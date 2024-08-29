package com.mata.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class GoodsAddDto {
    private Long goodsId; // 商品id

    @NotEmpty(message = "商品名长度要大于0，小于50")
    @Length(min = 1,max = 50,message = "商品名长度要大于0，小于50")
    private String goodsName; // 商品名

    @NotEmpty(message = "商品类型名长度要大于0，小于20")
    @Length(min = 1,max = 50,message = "商品类型名长度要大于0，小于20")
    private String goodsType; // 商品类型

    @Min(value = 0,message = "商品数量不能小于0")
    @NotNull(message = "商品数量不能小于0")
    private Integer goodsCount;  // 商品数量

    @Min(value = 1,message = "商品价格不能小于1")
    @NotNull(message = "商品价格不能小于0")
    private Double goodsPrice; // 商品价格

    @NotEmpty(message = "商品连接方式长度要大于0，小于20")
    @Length(min = 1,max = 50,message = "商品连接方式长度要大于0，小于20")
    private String goodsConnectionType; // 商品连接方式

    @NotNull(message = "商品图片不能为空")
    private MultipartFile goodsUrl; // 商品图片url

    @NotNull(message = "商品介绍不能为空")
    @NotEmpty(message = "商品介绍不能为空")
    private String goodsIntroduction; //商品介绍

    @Length(min = 1,max = 20,message = "商品品牌名长度要大于0，小于50")
    @NotEmpty(message = "商品品牌名长度要大于0，小于50")
    private String goodsBrand; // 商品品牌


}
