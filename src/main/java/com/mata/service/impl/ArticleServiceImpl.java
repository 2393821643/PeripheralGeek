package com.mata.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.EsDoc.ArticleDoc;
import com.mata.dao.ArticleDao;
import com.mata.dao.ArticleDocDao;
import com.mata.dto.ArticleDto;
import com.mata.dto.ArticleUpdateDto;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.holder.Holder;
import com.mata.pojo.Article;
import com.mata.pojo.Order;
import com.mata.service.ArticleService;
import com.mata.utils.CosClientUtil;
import com.mata.utils.RedisCommonKey;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {
    @Autowired
    @Qualifier("articleBloom")
    private RBloomFilter<Long> articleBloom;

    @Autowired
    @Qualifier("userBloom")
    private RBloomFilter<Integer> userBloom;

    @Value("${file.path}")
    private String filePath;

    @Autowired
    private CosClientUtil cosClientUtil;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ArticleDocDao articleDocDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 添加文章
     */
    @Override
    public Result addArticle(ArticleDto articleDto) {
        long articleId = IdUtil.getSnowflakeNextId();
        // 生成文章id,加入bloom
        articleBloom.add(articleId);
        // 发送图片
        CompletableFuture<String> writeImg = CompletableFuture.supplyAsync(() -> {
            try {
                MultipartFile articleUrlFile = articleDto.getArticleImg();
                byte[] imgBytes = articleUrlFile.getBytes();
                return writeToCos(imgBytes, articleUrlFile.getOriginalFilename(), CosFileMkdir.ArticleImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // 发送内容html
        CompletableFuture<String> writeIntroduction = CompletableFuture.supplyAsync(() -> {
            try {
                byte[] introductionBytes = articleDto.getContext().getBytes();
                return writeToCos(introductionBytes, IdUtil.getSnowflakeNextIdStr() + ".html", CosFileMkdir.ArticleHtmlImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        String articleImgUrl = writeImg.join(); // 回调返回url
        String articleContextUrl = writeIntroduction.join(); // 回调返回url
        // 拼装原生article对象
        Article article = Article.builder()
                .articleId(articleId)
                .articleTitle(articleDto.getTitle())
                .articleContextUrl(articleContextUrl)
                .articleImgUrl(articleImgUrl)
                .createTime(LocalDateTime.now())
                .userId(Holder.getUser())
                .articleState("已审核")
                .build();
        // 发送信息队列
        rabbitTemplate.convertAndSend("ArticleExchange", "addArticleKey", JSONUtil.toJsonStr(article));
        return Result.success("提交文章成功");
    }

    /**
     * 删除缓存前缀key
     */
    private void deleteKeysByPrefix(String prefix) {
        // 使用SCAN命令遍历所有匹配的键
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(100).build();
        try (Cursor<byte[]> cursor = stringRedisTemplate.getConnectionFactory().getConnection().scan(options)) {
            while (cursor.hasNext()) {
                byte[] keyBytes = cursor.next();
                String key = new String(keyBytes);
                stringRedisTemplate.delete(key);
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        }
    }

    /**
     * 写入文件
     */
    private String writeToCos(byte[] fileByte, String fileName, CosFileMkdir cosFileMkdir) throws IOException {
        // 设置文件对象
        File convertedFile = new File(filePath + fileName);
        FileOutputStream fos = new FileOutputStream(convertedFile);
        // 写入本地文件
        fos.write(fileByte);
        fos.close();
        //写入cos
        String imgUrl = cosClientUtil.sendFile(convertedFile, cosFileMkdir);
        // 删除本地文件
        convertedFile.delete();
        return imgUrl;
    }

    /**
     * 添加到Mysql
     */
    public void addArticleToMysql(Article article) {
        save(article);
        // 缓存写入
        String articleJson = JSONUtil.toJsonStr(article);
        stringRedisTemplate.opsForValue().set(RedisCommonKey.ARTICLE_PRE_KEY + article.getArticleId(), articleJson, RedisCommonKey.ARTICLE_TIME, TimeUnit.MINUTES);
    }

    /**
     * 添加到Es
     */
    public void addArticleToEs(Article article) {
        ArticleDoc articleDoc = new ArticleDoc(article);
        articleDocDao.addArticle(articleDoc);

    }

    /**
     * 根据用户id 获取文章列表
     */
    @Override
    public Result<PageResult<Article>> getArticleByUserId(Integer userId, Integer page) {
        PageResult<Article> resultPage = null;
        // 查看用户是否存在
        boolean isExist = userBloom.contains(userId);
        if (!isExist) {
            return Result.error("用户不存在");
        }
        // 查Redis
        String resultPageJson = stringRedisTemplate.opsForValue().get(RedisCommonKey.ARTICLE_USER_PRE_KEY + userId + ":" + page);
        if (!StrUtil.isEmpty(resultPageJson)) {
            resultPage = JSONUtil.toBean(resultPageJson, PageResult.class);
            return Result.success(resultPage);
        }
        // 加锁，查数据库
        RLock lock = redissonClient.getLock(RedisCommonKey.ARTICLE_USER_LOCK_PRE_KEY + userId + ":" + page);
        try {
            boolean isLock = lock.tryLock(0, RedisCommonKey.ARTICLE_USER_LOCK_TIME, TimeUnit.SECONDS);
            if (isLock) {
                Page<Article> articlePage = lambdaQuery()
                        .select(Article::getArticleId, Article::getArticleTitle, Article::getArticleImgUrl, Article::getArticleContextUrl)
                        .eq(Article::getUserId, userId)
                        .eq(Article::getArticleState, "已审核")
                        .orderByDesc(Article::getCreateTime)
                        .page(new Page<>(page, 20));
                resultPage = new PageResult<>(articlePage.getTotal(), articlePage.getRecords());
                // 写入缓存
                stringRedisTemplate.opsForValue().set(RedisCommonKey.ARTICLE_USER_PRE_KEY + userId + ":" + page, JSONUtil.toJsonStr(resultPage), RedisCommonKey.ARTICLE_USER_TIME, TimeUnit.MINUTES);
            } else {
                Thread.sleep(50);
                getArticleByUserId(userId, page);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 解开锁
            boolean heldByCurrentThread = lock.isHeldByCurrentThread();
            if (heldByCurrentThread) {
                lock.unlock();
            }
        }
        return Result.success(resultPage);
    }

    /**
     * 根据文章id获取文章
     */
    @Override
    public Result<Article> getArticleById(Long articlesId) {
        Article article = null;
        // 查bloom 是否存在此文章
        boolean isExist = articleBloom.contains(articlesId);
        if (!isExist) {
            return Result.error("不存在此文章");
        }
        // 查redis
        String articleJson = stringRedisTemplate.opsForValue().get(RedisCommonKey.ARTICLE_PRE_KEY + articlesId);
        if (!StrUtil.isEmpty(articleJson)) {
            article = JSONUtil.toBean(articleJson, Article.class);
            if (Objects.equals(article.getArticleState(), "未审核")){
                return Result.error("不存在此文章");
            }
            return Result.success(article);
        }
        // 加锁，查数据库
        RLock lock = redissonClient.getLock(RedisCommonKey.ARTICLE_LOCK_PRE_KEY + articlesId);
        try {
            boolean isLock = lock.tryLock(0, RedisCommonKey.ARTICLE_LOCK_TIME, TimeUnit.SECONDS);
            if (isLock) {

                LambdaQueryWrapper<Article> wapper = new LambdaQueryWrapper<>();
                wapper.select(Article::getArticleId, Article::getArticleTitle, Article::getArticleImgUrl, Article::getArticleContextUrl)
                        .eq(Article::getArticleId, articlesId)
                        .eq(Article::getArticleState, "已审核");
                article = getOne(wapper);
                // 写入缓存
                stringRedisTemplate.opsForValue().set(RedisCommonKey.ARTICLE_PRE_KEY + articlesId,JSONUtil.toJsonStr(article),RedisCommonKey.ARTICLE_TIME,TimeUnit.MINUTES);
            } else {
                Thread.sleep(50);
                getArticleById(articlesId);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 解开锁
            boolean heldByCurrentThread = lock.isHeldByCurrentThread();
            if (heldByCurrentThread) {
                lock.unlock();
            }
        }
        return Result.success(article);
    }

    /**
     * 根据文章名获取文章
     */
    @Override
    public Result<PageResult<ArticleDoc>> getArticleByName(String articleName,Integer page) {
        PageResult<ArticleDoc> resultPage = articleDocDao.getArticleByName(articleName, page);
        return Result.success(resultPage);
    }

    /**
     *  删除文章
     */
    @Override
    public Result deleteArticleById(Long articleId) {
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, Holder.getUser());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 删除缓存
        stringRedisTemplate.delete(RedisCommonKey.ARTICLE_PRE_KEY+articleId);
        deleteKeysByPrefix(RedisCommonKey.ARTICLE_USER_PRE_KEY+Holder.getUser());
        // 异步删除
        rabbitTemplate.convertAndSend("ArticleExchange","deleteArticleKey",articleId.toString());
        return Result.success("删除成功");
    }

    /**
     * 检查文章和用户是否对应
     */
    private boolean checkArticleIsUserHave(Long articleId,Integer userId){
        boolean contains = articleBloom.contains(articleId);
        if (!contains){
            return false;
        }
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getArticleId)
                .eq(Article::getArticleId,articleId)
                .eq(Article::getUserId,userId);
        Article a = getOne(wrapper);
        return a != null;
    }

    /**
     * 从mysql中删除
     */
    public void deleteToMysql(Long articleId){
        removeById(articleId);
    }

    /**
     *  删除文章从es
     */
    public void deleteToEs(String articleId){
        articleDocDao.deleteArticle(articleId);
    }

    /**
     * 修改文章标题，内容 通过文章Id
     */
    @Override
    public Result updateArticle(ArticleUpdateDto articleUpdateDto) {
        Long articleId = articleUpdateDto.getArticleId();
        String articleContextUrl = null;
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, Holder.getUser());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 删除缓存
        stringRedisTemplate.delete(RedisCommonKey.ARTICLE_PRE_KEY+articleId);
        deleteKeysByPrefix(RedisCommonKey.ARTICLE_USER_PRE_KEY+Holder.getUser());
        // 查找源文章信息
        Article article = getById(articleId);
        // 更新文章
        try {
            byte[] introductionBytes = articleUpdateDto.getContext().getBytes();
            articleContextUrl =  writeToCos(introductionBytes, IdUtil.getSnowflakeNextIdStr() + ".html", CosFileMkdir.ArticleHtmlImg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 重新构建文章对象
        article.setArticleTitle(articleUpdateDto.getTitle());
        article.setArticleContextUrl(articleContextUrl);
        // 异步发送修改
        rabbitTemplate.convertAndSend("ArticleExchange","updateArticleKey",JSONUtil.toJsonStr(article));
        return Result.success("修改成功");
    }

    /**
     * 修改文章信息到Mysql
     */
    @Override
    public void updateToMysql(Article article) {
        updateById(article);
    }

    /**
     * 修改文章信息到es
     */
    @Override
    public void updateToEs(Article article) {
        articleDocDao.updateArticle(new ArticleDoc(article));
    }

    /**
     * 修改文章图片 通过文章Id
     */
    @Override
    public Result updateArticleImg(Long articleId, MultipartFile img) {
        String articleImgUrl = null;
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, Holder.getUser());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 删除缓存
        stringRedisTemplate.delete(RedisCommonKey.ARTICLE_PRE_KEY+articleId);
        deleteKeysByPrefix(RedisCommonKey.ARTICLE_USER_PRE_KEY+Holder.getUser());
        // 查找源文章信息
        Article article = getById(articleId);
        try {
            byte[] imgBytes = img.getBytes();
            articleImgUrl = writeToCos(imgBytes, img.getOriginalFilename(), CosFileMkdir.ArticleImg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        article.setArticleImgUrl(articleImgUrl);
        // 异步发送
        rabbitTemplate.convertAndSend("ArticleExchange","updateArticleKey",JSONUtil.toJsonStr(article));
        return Result.success("修改成功");
    }


}
