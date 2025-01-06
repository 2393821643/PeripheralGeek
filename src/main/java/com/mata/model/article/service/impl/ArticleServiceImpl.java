package com.mata.model.article.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.common.result.Suggest;
import com.mata.model.article.dao.AuditDao;
import com.mata.model.article.dto.ArticleAuditDto;
import com.mata.model.article.dto.ArticleSearchDto;
import com.mata.model.article.esDoc.ArticleDoc;
import com.mata.model.article.dao.ArticleDao;
import com.mata.model.article.esDao.ArticleDocDao;
import com.mata.model.article.dto.ArticleDto;
import com.mata.model.article.dto.ArticleUpdateDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.common.enumPackage.CosFileMkdir;
import com.mata.model.article.vo.ArticleAuditVo;
import com.mata.model.article.vo.ArticleVo;
import com.mata.model.user.dao.UserDao;
import com.mata.pojo.Article;
import com.mata.model.article.service.ArticleService;
import com.mata.pojo.Audit;
import com.mata.pojo.User;
import com.mata.utils.CosClientUtil;
import com.mata.utils.HtmlUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuditDao auditDao;


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
        // 获取前50字内容(简介)
        CompletableFuture<String> getBriefIntroduction = CompletableFuture.supplyAsync(() -> {
            return HtmlUtil.getParagraphsUntil50Chars(articleDto.getContext());
        });
        String articleImgUrl = writeImg.join(); // 回调返回url
        String articleContextUrl = writeIntroduction.join(); // 回调返回url
        String briefIntroduction = getBriefIntroduction.join(); // 获取简介
        // 拼装原生article对象
        Article article = Article.builder()
                .articleId(articleId)
                .articleTitle(articleDto.getArticleTitle())
                .articleContextUrl(articleContextUrl)
                .articleImgUrl(articleImgUrl)
                .briefIntroduction(briefIntroduction)
                .createTime(LocalDateTime.now())
                .userId(StpUtil.getLoginIdAsInt())
                .articleState("已审核")
                .build();
        save(article);
        addArticleToEs(article);
        return Result.success("提交文章成功");
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
    public Result<PageResult<Article>> getArticleByUserId(ArticleSearchDto articleSearchDto) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Article::getArticleId, Article::getArticleTitle, Article::getArticleImgUrl, Article::getArticleContextUrl,Article::getBriefIntroduction,Article::getCreateTime)
                .eq(Article::getUserId, articleSearchDto.getUserId())
                .eq(Article::getArticleState, "已审核")
                .orderByDesc(Article::getCreateTime);

        // 如果没有标题，就不模糊查询
        if (!StrUtil.isEmpty(articleSearchDto.getTitle())){
            wrapper.likeRight(Article::getArticleTitle,articleSearchDto.getTitle());
        }
        // 配置页
        Page<Article> articlePage = new Page(articleSearchDto.getPage(), 20);
        // 查询
        articlePage = page(articlePage, wrapper);
        PageResult<Article> resultPage = new PageResult<>(articlePage.getTotal(), articlePage.getRecords());
        return Result.success(resultPage);
    }

    /**
     * 根据文章id获取文章
     */
    @Override
    public Result<ArticleVo> getArticleById(Long articlesId) {
        ArticleVo articleVo = baseMapper.getArticleById(articlesId);
        return Result.success(articleVo);
    }

    /**
     * 根据文章名获取文章
     */
    @Override
    public Result<PageResult<ArticleVo>> getArticleByName(String articleName,Integer page) {
        PageResult<ArticleDoc> resultPage = articleDocDao.getArticleByName(articleName, page);
        // 返回的pageResult
        PageResult<ArticleVo> articleVoPageResult = new PageResult<>(0,new ArrayList<>());
        articleVoPageResult.setTotal(resultPage.getTotal());
        // 将resultPage中的ArticleDoc转为ArticleVo(将user的信息注入)
        List<ArticleDoc> articleDocList = resultPage.getRecords();
        List<Integer> userIdList = new ArrayList<>();
        for (ArticleDoc articleDoc:articleDocList){
            userIdList.add(articleDoc.getUserId());
        }
        // 批量查询 如果userIdList为空直接返回空
        if (userIdList.isEmpty()){
           return Result.success(articleVoPageResult);
        }
        List<User> users = userDao.selectBatchIds(userIdList);
        // 组装user信息
        for (ArticleDoc articleDoc : articleDocList){
            for (User user : users){
                if (Objects.equals(articleDoc.getUserId(), user.getUserId())){
                    ArticleVo articleVo = BeanUtil.copyProperties(articleDoc, ArticleVo.class);
                    articleVo.setUsername(user.getUsername());
                    articleVo.setHeadUrl(user.getHeadUrl());
                    articleVoPageResult.getRecords().add(articleVo);
                }
            }
        }
        return Result.success(articleVoPageResult);
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
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, StpUtil.getLoginIdAsInt());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 查找源文章信息
        Article article = getById(articleId);
        // 更新文章
        // 发送内容到cos
        CompletableFuture<String> writeIntroduction = CompletableFuture.supplyAsync(() -> {
            try {
                byte[] introductionBytes = articleUpdateDto.getContext().getBytes();
                return writeToCos(introductionBytes, IdUtil.getSnowflakeNextIdStr() + ".html", CosFileMkdir.ArticleHtmlImg);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // 获取前50字内容(简介)
        CompletableFuture<String> getBriefIntroduction = CompletableFuture.supplyAsync(() -> {
            return HtmlUtil.getParagraphsUntil50Chars(articleUpdateDto.getContext());
        });
        String articleContextUrl = writeIntroduction.join();
        String briefIntroduction = getBriefIntroduction.join();
        // 重新构建文章对象
        article.setArticleTitle(articleUpdateDto.getArticleTitle());
        article.setArticleContextUrl(articleContextUrl);
        article.setBriefIntroduction(briefIntroduction);
        // 修改
        articleDocDao.updateArticle(new ArticleDoc(article));
        updateById(article);
        return Result.success("修改成功");
    }
    /**
     * 修改文章图片 通过文章Id
     */
    @Override
    public Result<String> updateArticleImg(Long articleId, MultipartFile img) {
        String articleImgUrl = null;
        // 检查文章是否存在
        boolean exist = checkArticleIsUserHave(articleId, StpUtil.getLoginIdAsInt());
        if (!exist){
            return Result.error("此文章不存在");
        }
        // 查找源文章信息
        Article article = getById(articleId);
        try {
            byte[] imgBytes = img.getBytes();
            articleImgUrl = writeToCos(imgBytes, img.getOriginalFilename(), CosFileMkdir.ArticleImg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        article.setArticleImgUrl(articleImgUrl);
        updateById(article);
        return Result.success(articleImgUrl,"修改成功");
    }

    /**
     * 获取文章推荐词
     */
    @Override
    public Result<List<Suggest>> getSuggest(String articleName) {
        List<String> suggestionStr = articleDocDao.getSuggestions(articleName);
        List<Suggest> suggestions = new ArrayList<>();
        suggestionStr.forEach(suggest->suggestions.add(new Suggest(suggest)));
        return Result.success(suggestions);
    }

    /**
     * 获取文章列表 根据审核状态
     */
    @Override
    public Result<PageResult<ArticleAuditVo>> getArticleByState(ArticleSearchDto articleSearchDto) {
        // 获取当前账号权限
        List<String> roleList = StpUtil.getRoleList();
        String role = roleList.get(0);
        // 如果账号是user,只能看到自己的文章
        if (Objects.equals(role, "user")){
            articleSearchDto.setUserId(StpUtil.getLoginIdAsInt());
        }
        // 配置页
        Page<ArticleAuditVo> articlePage = new Page(articleSearchDto.getPage(), 20);
        // 查询
        IPage<ArticleAuditVo> articleAuditList = baseMapper.getArticleAuditList(articlePage, articleSearchDto);
        PageResult<ArticleAuditVo> resultPage = new PageResult<>(articleAuditList.getTotal(), articleAuditList.getRecords());
        return Result.success(resultPage);
    }

    /**
     * 管理员审核文章
     */
    @Override
    public Result auditArticle(ArticleAuditDto articleAuditDto) {
        Integer auditCode = articleAuditDto.getAuditCode();
        Article resultArticle = getById(articleAuditDto.getArticleId());
        // 如果通过审核
        if (auditCode == 1){
            // 删除未通过审核记录
            auditDao.deleteById(articleAuditDto.getArticleId());
            resultArticle.setArticleState("已审核");
            // 修改es
            articleDocDao.updateArticle(new ArticleDoc(resultArticle));
        }else if (auditCode == 2 ){
            // 如果没通过审核
            resultArticle.setArticleState("审核未通过");
            // 更新未通过审核记录
            Audit audit = new Audit(articleAuditDto.getArticleId(), articleAuditDto.getNonPassCause());
            saveOrUpdateAudit(audit);
        }
        // 修改数据库文章状态
        updateById(resultArticle);
        // 修改es
        articleDocDao.updateArticle(new ArticleDoc(resultArticle));
        return Result.success("修改成功");
    }

    /**
     * 新增或更新审核记录
     */
    private void saveOrUpdateAudit(Audit audit){
        // 查找此记录是否存在
        boolean exists = auditDao.exists(new LambdaQueryWrapper<Audit>().eq(Audit::getArticleId, audit.getArticleId()));
        if (exists){
            auditDao.updateById(audit);
        }else {
            auditDao.insert(audit);
        }
    }
}
