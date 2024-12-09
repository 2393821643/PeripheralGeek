package com.mata.model.article.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.model.article.esDoc.ArticleDoc;
import com.mata.model.article.dao.ArticleDao;
import com.mata.model.article.esDao.ArticleDocDao;
import com.mata.model.article.dto.ArticleDto;
import com.mata.model.article.dto.ArticleUpdateDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.common.enumPackage.CosFileMkdir;
import com.mata.pojo.Article;
import com.mata.model.article.service.ArticleService;
import com.mata.utils.CosClientUtil;
import com.mata.common.redisKey.RedisCommonKey;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.CompletableFuture;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleDao, Article> implements ArticleService {
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


    /**
     * 添加文章
     */
    @Override
    public Result addArticle(ArticleDto articleDto) {
        long articleId = IdUtil.getSnowflakeNextId();
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
                .userId(StpUtil.getLoginIdAsInt())
                .articleState("已审核")
                .build();
        // 发送信息队列
        save(article);
        addArticleToEs(article);
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
     * 添加到Es
     */
    private void addArticleToEs(Article article) {
        ArticleDoc articleDoc = new ArticleDoc(article);
        articleDocDao.addArticle(articleDoc);

    }

    /**
     * 根据用户id 获取文章列表
     */
    @Override
    public Result<PageResult<Article>> getArticleByUserId(Integer userId, Integer page) {
        Page<Article> articlePage = lambdaQuery()
                .select(Article::getArticleId, Article::getArticleTitle, Article::getArticleImgUrl, Article::getArticleContextUrl)
                .eq(Article::getUserId, userId)
                .eq(Article::getArticleState, "已审核")
                .orderByDesc(Article::getCreateTime)
                .page(new Page<>(page, 20));
        PageResult<Article> resultPage = new PageResult<>(articlePage.getTotal(), articlePage.getRecords());
        return Result.success(resultPage);
    }

    /**
     * 根据文章id获取文章
     */
    @Override
    public Result<Article> getArticleById(Long articlesId) {
        LambdaQueryWrapper<Article> wapper = new LambdaQueryWrapper<>();
        wapper.select(Article::getArticleId, Article::getArticleTitle, Article::getArticleImgUrl, Article::getArticleContextUrl)
                .eq(Article::getArticleId, articlesId)
                .eq(Article::getArticleState, "已审核");
        Article article = getOne(wapper);
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
        boolean exist = checkArticleIsUserHave(articleId, StpUtil.getLoginIdAsInt());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 删es
        articleDocDao.deleteArticle(articleId.toString());
        // 删mysql
        removeById(articleId);
        return Result.success("删除成功");
    }

    /**
     * 检查文章和用户是否对应
     */
    private boolean checkArticleIsUserHave(Long articleId,Integer userId){
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getArticleId)
                .eq(Article::getArticleId,articleId)
                .eq(Article::getUserId,userId);
        Article article = getOne(wrapper);
        return article != null;
    }

    /**
     * 修改文章标题，内容 通过文章Id
     */
    @Override
    public Result updateArticle(ArticleUpdateDto articleUpdateDto) {
        Long articleId = articleUpdateDto.getArticleId();
        String articleContextUrl = null;
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, StpUtil.getLoginIdAsInt());
        if (!exist){
            return Result.error("此文章不存在");
        }
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
        // 修改
        articleDocDao.updateArticle(new ArticleDoc(article));
        updateById(article);
        return Result.success("修改成功");
    }
    /**
     * 修改文章图片 通过文章Id
     */
    @Override
    public Result updateArticleImg(Long articleId, MultipartFile img) {
        String articleImgUrl = null;
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, StpUtil.getLoginIdAsInt());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 删除缓存
        stringRedisTemplate.delete(RedisCommonKey.ARTICLE_PRE_KEY+articleId);
        deleteKeysByPrefix(RedisCommonKey.ARTICLE_USER_PRE_KEY+StpUtil.getLoginIdAsInt());
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
