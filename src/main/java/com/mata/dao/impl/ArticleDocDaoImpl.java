package com.mata.dao.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mata.EsDoc.ArticleDoc;
import com.mata.dao.ArticleDocDao;
import com.mata.dto.PageResult;
import com.mata.dto.Result;
import com.mata.pojo.Goods;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 根据文章名获取文章
     */
    @Override
    public PageResult<ArticleDoc> getArticleByName(String articleName,Integer page) {
        // 创建请求对象
        SearchRequest searchRequest = new SearchRequest("article");
        // DSL
        // 查看搜索商品名是不是空，空就返回推荐商品
        if (StrUtil.isEmpty(articleName)){
            searchRequest.source()
                    .query(QueryBuilders.matchAllQuery());
        }else {
            searchRequest.source()
                    .query(QueryBuilders.matchQuery("all",articleName));
        }
        // 分页 一次20个结果
        searchRequest.source()
                .from((page - 1) * 20)
                .size(20);
        // 发送请求 解析响应
        SearchResponse response = null;
        try {
            response = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return handleResponse(response);
    }

    /**
     * 封装解析搜索
     */
    private PageResult<ArticleDoc> handleResponse(SearchResponse response){
        List<ArticleDoc> goodsList = new ArrayList<>();
        //解析操作
        SearchHits searchHits = response.getHits();
        //查询总条数
        long total = searchHits.getTotalHits().value;
        //查询结果数组
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit:hits){
            //4.3获得source
            String articleDocJson = hit.getSourceAsString();
            ArticleDoc articleDoc = JSONUtil.toBean(articleDocJson, ArticleDoc.class);
            goodsList.add(articleDoc);
        }
        return new PageResult<>(total,goodsList);
    }

    /**
     * 删除文章
     */
    @Override
    public void deleteArticle(String articleId) {
        //1：准备Request对象
        DeleteRequest request = new DeleteRequest("article", articleId);
        //2:发送请求
        try {
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改文章信息
     */
    @Override
    public void updateArticle(ArticleDoc articleDoc) {
        deleteArticle(articleDoc.getArticleId().toString());
        addArticle(articleDoc);
    }
}
