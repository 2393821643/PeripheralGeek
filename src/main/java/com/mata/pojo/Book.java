package com.mata.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("tb_book")
public class Book {
    @TableId(value="book_id", type= IdType.NONE)
    private Long bookId; // 书本id

    @TableField("book_name")
    private String bookName; // 书本名称

    @TableField("author")
    private String author; // 书籍作者

    @TableField("publisher")
    private String publisher; // 书籍出版社

    @TableField("price")
    private Double price; // 书籍价格

    @TableField("book_introduction")
    private String bookIntroduction; // 书籍介绍

    @TableField("book_url")
    private String bookUrl; // 书籍图片url
}
