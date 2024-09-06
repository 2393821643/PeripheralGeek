package com.mata.listener;

import cn.hutool.json.JSONUtil;
import com.mata.pojo.Article;
import com.mata.pojo.Goods;
import com.mata.service.ArticleService;
import com.mata.service.GoodsService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArticleMessageListener {
    @Autowired
    private ArticleService articleService;

    /**
     * 添加文章信息 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "addArticleQueue"),
            exchange = @Exchange(name = "ArticleExchange",type = ExchangeTypes.DIRECT),
            key = {"addArticleKey"}
    ))
    public void addArticles(String articleJson){
        Article article = JSONUtil.toBean(articleJson, Article.class);
        articleService.addArticleToMysql(article);
        articleService.addArticleToEs(article);
    }

    /**
     * 添加删除文章信息 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "deleteArticleQueue"),
            exchange = @Exchange(name = "ArticleExchange",type = ExchangeTypes.DIRECT),
            key = {"deleteArticleKey"}
    ))
    public void deleteArticles(String articleIdStr){
        Long articleId = Long.valueOf(articleIdStr);
        articleService.deleteToMysql(articleId);
        articleService.deleteToEs(articleIdStr);
    }

    /**
     * 修改文章信息 到es和mysql
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "updateArticleQueue"),
            exchange = @Exchange(name = "ArticleExchange",type = ExchangeTypes.DIRECT),
            key = {"updateArticleKey"}
    ))
    public void updateArticles(String articleJson){
        Article article = JSONUtil.toBean(articleJson, Article.class);
        articleService.updateToMysql(article);
        articleService.updateToEs(article);
    }

//    /**
//     * 修改文章图片 到es和mysql
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(name = "updateArticleImgQueue"),
//            exchange = @Exchange(name = "ArticleExchange",type = ExchangeTypes.DIRECT),
//            key = {"updateArticleImgKey"}
//    ))
//    public void updateArticles(String articleJson){
//        Article article = JSONUtil.toBean(articleJson, Article.class);
//        articleService.updateToMysql(article);
//        articleService.updateImgToEs(article);
//    }
}
