package com.mata.controller;

import com.mata.dto.GoodsAddDto;
import com.mata.dto.GoodsUpdateDto;
import com.mata.dto.Result;
import com.mata.service.GoodsService;
import lombok.AllArgsConstructor;
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
    public Result addGoods(@Validated GoodsAddDto goodsAddDto) {
        return goodsService.addGoods(goodsAddDto);
    }

    /**
     * 管理员删除商品
     */
    @DeleteMapping("/admin/{goodsId}")
    public Result deleteGoods(@PathVariable("goodsId") Long goodId){
        return goodsService.deleteGoods(goodId);
    }

    /**
     * 管理员修改商品
     */
    @PutMapping("/admin")
    public Result updateGoods(@RequestBody @Validated GoodsUpdateDto goodsUpdateDto){
        return goodsService.updateGoods(goodsUpdateDto);
    }

    /**
     * 管理员修改商品图片
     */
    @PutMapping("/admin/img/{goodsId}")
    public Result updateGoodsImg(@PathVariable("goodsId")Long goodsId, @RequestParam MultipartFile goodsImg){
        return goodsService.updateGoodsImg(goodsId,goodsImg);
    }

    /**
     * 管理员修改商品介绍
     */
    @PutMapping("/admin/introduction/{goodsId}")
    public Result updateGoodsIntroduction(@PathVariable("goodsId")Long goodsId, @RequestParam String goodsIntroduction){
        return goodsService.updateGoodsInformation(goodsId,goodsIntroduction);
    }

    /**
     * 返回商品推荐词
     */
    @GetMapping("/suggest/{goodsName}")
    public Result<List<String>> getSuggestions(@PathVariable("goodsName") String goodsName){
        return goodsService.getSuggestions(goodsName);
    }


}
