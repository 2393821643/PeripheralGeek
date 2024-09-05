package com.mata.dao.impl;

import cn.hutool.json.JSONUtil;
import com.mata.EsDoc.ArticleDoc;
import com.mata.dao.ArticleDocDao;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

@Repository
public class ArticleDocDaoImpl implements ArticleDocDao {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 添加文章
     */
    @Override
    public void addArticle(ArticleDoc articleDoc) {
        //获取对象
        String articleDocJson = JSONUtil.toJsonStr(articleDoc);
        //1：准备Request对象
        IndexRequest request = new IndexRequest("article").id(articleDoc.getArticleId().toString());
        //2：准备Json
        request.source(articleDocJson, XContentType.JSON);
        //3:发请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
