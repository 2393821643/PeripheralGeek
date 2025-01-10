package com.mata.model.shoppingCart.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.shoppingCart.dao.ShoppingCartDao;
import com.mata.model.shoppingCart.dto.ShoppingCartAddDto;
import com.mata.model.shoppingCart.dto.ShoppingCartSearchDto;
import com.mata.model.shoppingCart.service.ShoppingCartService;
import com.mata.model.shoppingCart.vo.ShoppingCartVo;
import com.mata.pojo.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao, ShoppingCart> implements ShoppingCartService {
    @Autowired
    private ShoppingCartDao shoppingCartDao;
    /**
     * 新增购物车商品
     */
    @Override
    public Result<Integer> addGoodsToShoppingCart(ShoppingCartAddDto shoppingCartAddDto) {
        // 检查此商品是否在此用户的购物车
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getGoodsId, shoppingCartAddDto.getGoodsId())
                .eq(ShoppingCart::getUserId, StpUtil.getLoginId());
        ShoppingCart shoppingCart = shoppingCartDao.selectOne(wrapper);
        // 没找到就是新增
        if (shoppingCart == null) {
            ShoppingCart copyShoppingCart = BeanUtil.copyProperties(shoppingCartAddDto, ShoppingCart.class);
            copyShoppingCart.setUserId(StpUtil.getLoginIdAsInt());
            this.save(copyShoppingCart);
            return Result.success(copyShoppingCart.getId());
        }else {
            // 找到就是修改
            shoppingCart.setGoodsWillBuyCount(shoppingCartAddDto.getGoodsWillBuyCount());
            updateById(shoppingCart);
            return Result.success(shoppingCart.getId());
        }
    }

    /**
     * 获取购物车列表
     */
    @Override
    public Result<PageResult<ShoppingCartVo>> getShoppingCartList(ShoppingCartSearchDto shoppingCartSearchDto) {
        shoppingCartSearchDto.setUserId(StpUtil.getLoginIdAsInt());
        IPage<ShoppingCartVo> shoppingCartList = baseMapper.getShoppingCartList(new Page<ShoppingCartVo>(shoppingCartSearchDto.getPageNum(), 20), shoppingCartSearchDto);
        PageResult<ShoppingCartVo> pageResult = new PageResult<>(shoppingCartList.getTotal(),shoppingCartList.getRecords());
        return Result.success(pageResult);
    }

    /**
     * 删除购物车商品
     */
    @Override
    public Result deleteShoppingCart(Integer shoppingCartId) {
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,StpUtil.getLoginIdAsInt())
                .eq(ShoppingCart::getId,shoppingCartId);
        boolean flag = remove(wrapper);
        if (flag){
            return Result.success("删除成功");
        }else {
            return Result.error("此商品不在购物车");
        }
    }
}
