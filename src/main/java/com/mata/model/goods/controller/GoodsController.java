package com.mata.model.goods.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.mata.common.result.Suggest;
import com.mata.model.goods.dto.GoodsAddDto;
import com.mata.model.goods.dto.GoodsUpdateDto;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.pojo.Goods;
import com.mata.model.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    /**
     * 管理员添加商品
     */
    @PostMapping("/admin")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result addGoods(@Validated GoodsAddDto goodsAddDto) {
        return goodsService.addGoods(goodsAddDto);
    }

    /**
     * 管理员删除商品
     */
    @DeleteMapping("/admin/{goodsId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result deleteGoods(@PathVariable("goodsId") Long goodId){
        return goodsService.deleteGoods(goodId);
    }

    /**
     * 管理员修改商品
     */
    @PutMapping("/admin")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result updateGoods(@RequestBody @Validated GoodsUpdateDto goodsUpdateDto){
        return goodsService.updateGoods(goodsUpdateDto);
    }

    /**
     * 管理员修改商品图片
     */
    @PutMapping("/admin/img/{goodsId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result updateGoodsImg(@PathVariable("goodsId")Long goodsId, @RequestParam MultipartFile goodsImg){
        return goodsService.updateGoodsImg(goodsId,goodsImg);
    }

    /**
     * 管理员修改商品介绍
     */
    @PutMapping("/admin/introduction/{goodsId}")
    @SaCheckLogin
    @SaCheckRole("admin")
    public Result updateGoodsIntroduction(@PathVariable("goodsId")Long goodsId, @RequestParam String goodsIntroduction){
        return goodsService.updateGoodsInformation(goodsId,goodsIntroduction);
    }

    /**
     * 返回商品推荐词
     */
    @GetMapping("/suggest/{goodsName}")
    public Result<List<Suggest>> getSuggestions(@PathVariable("goodsName") String goodsName){
        return goodsService.getSuggestions(goodsName);
    }

    /**
     * 搜索商品
     */
    @GetMapping("/search")
    public Result<PageResult<Goods>> getGoodsByName(@RequestParam("goodsName")String goodsName, @RequestParam("page")Integer page){
        return goodsService.getGoodsByName(goodsName,page);
    }

    /**
     * 搜索商品 通过id
     */
    @GetMapping("/{goodsId}")
    public Result<Goods> getGoodsById(@PathVariable("goodsId") Long goodsId){
        return goodsService.getGoodsById(goodsId);
    }





}
