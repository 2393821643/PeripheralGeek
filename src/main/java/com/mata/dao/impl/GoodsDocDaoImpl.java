package com.mata.dao.impl;

import cn.hutool.json.JSONUtil;
import com.mata.EsDoc.GoodsDoc;
import com.mata.dao.GoodsDocDao;
import com.mata.enumPackage.CosFileMkdir;
import com.mata.pojo.Goods;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GoodsDocDaoImpl implements GoodsDocDao {
    @Autowired
    private RestHighLevelClient client;

    /**
     * 新增商品
     */
    @Override
    public void addGoods(GoodsDoc goodsDoc) {
        //获取对象
        String goodsJson = JSONUtil.toJsonStr(goodsDoc);
        //1：准备Request对象
        IndexRequest request = new IndexRequest("goods").id(goodsDoc.getGoodsId().toString());
        //2：准备Json
        request.source(goodsJson, XContentType.JSON);
        //3:发请求
        try {
            client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteGoods(String goodsId) {
        //1：准备Request对象
        DeleteRequest request = new DeleteRequest("goods", goodsId);
        //2:发送请求
        try {
            client.delete(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 修改商品
     */
    @Override
    public void updateGoods(GoodsDoc goodsDoc) {
        deleteGoods(goodsDoc.getGoodsId().toString());
        addGoods(goodsDoc);
    }

    /**
     * 更新商品的图片或介绍的地址
     */
    @Override
    public void updateGoodsFile(Goods goods, CosFileMkdir cosFileMkdir) {
        //1：准备Request对象
        UpdateRequest request = new UpdateRequest("goods", goods.getGoodsId().toString());
        // 2:准备参数
        // 判断是图片还是介绍
        if (cosFileMkdir.equals(CosFileMkdir.GoodsImg)){
            request.doc(
                    "goodsUrl",goods.getGoodsUrl()
            );
        }else if (cosFileMkdir.equals(CosFileMkdir.GoodsHtmlImg)){
            request.doc(
                    "goodsIntroduction",goods.getGoodsIntroduction()
            );
        }
        //3：发送请求
        try {
            client.update(request,RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 返回商品推荐词
     */
    @Override
    public List<String> getSuggestions(String goodsName) {
        //准备请求
        SearchRequest request = new SearchRequest("goods");
        //DSL
        request.source()
                .suggest(new SuggestBuilder()
                        .addSuggestion("goods_suggestions", //这个是推荐suggest的名称 可自定义 比如define_suggest
                                SuggestBuilders.completionSuggestion("suggestion") // 这个是推荐字段的名称 在创建索引库的时候type为completion的字段名称
                                        .prefix(goodsName)
                                        .skipDuplicates(true)
                                        .size(10)));
        // 发送请求
        SearchResponse response = null;
        try {
            response = client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return handleSuggestsResponse(response);
    }
    /**
     * 解析suggestions
     * @param response
     */
    private List<String> handleSuggestsResponse(SearchResponse response){
        List<String> suggestionList = new ArrayList<>();
        Suggest suggest = response.getSuggest();
        CompletionSuggestion suggestion = suggest.getSuggestion("goods_suggestions");
        //遍历获取的suggest
        for (CompletionSuggestion.Entry.Option option : suggestion.getOptions()) {
            String suggestionItem = option.getText().string();
            suggestionList.add(suggestionItem);
        }
        return suggestionList;
    }
}
