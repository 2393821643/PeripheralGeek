package com.mata.model.article.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.common.redisKey.RedisCommonKey;
import com.mata.common.result.Result;
import com.mata.model.article.dao.ArticleDao;
import com.mata.model.article.dao.RecommendArticleDao;
import com.mata.model.article.service.RecommendArticleService;
import com.mata.model.article.vo.RecommendArticleVo;
import com.mata.pojo.Article;
import com.mata.pojo.RecommendArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@Service
public class RecommendArticleServiceImpl extends ServiceImpl<RecommendArticleDao, RecommendArticle> implements RecommendArticleService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleDao articleDao;

    /**
     * 新增推荐文章
     */
    @Override
    public Result addRecommendArticle(Long articleId) {
        // 查找此文章是否存在
        boolean exists = articleDao.exists(new LambdaQueryWrapper<Article>().eq(Article::getArticleId, articleId));
        if (!exists){
            return Result.error("此文章不存在");
        }
        try {
            save(new RecommendArticle(articleId));
        }catch (DataAccessException e){
            return Result.error("此文件已是推荐文章");
        }
        // 重建缓存
        rebuildCache();
        return Result.success("添加成功");
    }

    /**
     * 获取推荐文章
     */
    @Override
    public Result<List<RecommendArticleVo>> getRecommendArticle() {
        ListOperations<String, RecommendArticleVo> listOperations = redisTemplate.opsForList();
        List<RecommendArticleVo> recommendArticleList = listOperations.range(RedisCommonKey.RECOMMEND_ARTICLE_LIST_KEY, 0, -1);
        if (recommendArticleList.isEmpty()){
            recommendArticleList = baseMapper.getAllRecommendArticle();
        }
        return Result.success(recommendArticleList);
    }

    /**
     * 删除推荐文章
     */
    @Override
    public Result deleteRecommendArticle(Long articleId) {
        boolean isDelete = removeById(articleId);
        if (isDelete){
            rebuildCache();
        }
        return Result.success("删除成功");
    }


    /**
     * 重建缓存
     */
    public void rebuildCache(){
        // 删除缓存
        redisTemplate.delete(RedisCommonKey.RECOMMEND_ARTICLE_LIST_KEY);
        // 新建缓存
        List<RecommendArticleVo> allRecommendArticle = baseMapper.getAllRecommendArticle();
        ListOperations<String, RecommendArticleVo> listOperations = redisTemplate.opsForList();
        listOperations.leftPushAll(RedisCommonKey.RECOMMEND_ARTICLE_LIST_KEY,allRecommendArticle);
    }
}
