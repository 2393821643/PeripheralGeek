package com.mata.model.comment.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.hutool.log.Log;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.comment.dto.CommentDto;
import com.mata.model.comment.service.CommentService;
import com.mata.model.comment.vo.CommentVo;
import lombok.Data;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    /**
     * 评论商品/文章/某个评论
     */
    @PostMapping
    @SaCheckLogin
    @SaCheckRole("user")
    public Result comment(@RequestBody @Validated CommentDto commentDto){
        return commentService.comment(commentDto);
    }

    /**
     * 获取评论 根据从属id
     */
    @GetMapping("/root/{targetId}/{page}")
    public Result<PageResult<CommentVo>> getComment(@PathVariable("targetId") Long targetId,
                                                    @PathVariable("page") Integer page){
        return commentService.getComment(targetId,page);
    }

    /**
     * 点赞某个评论
     */
    @PostMapping("/good/{commentId}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result toGoodComment(@PathVariable("commentId") Long commentId){
        return commentService.toGoodComment(commentId);
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/good/{commentId}")
    @SaCheckLogin
    @SaCheckRole("user")
    public Result deleteGoodComment(@PathVariable("commentId") Long commentId){
        return commentService.deleteGoodComment(commentId);
    }

    /**
     * 删除评论
     */
    @DeleteMapping
    @SaCheckLogin
    public Result deleteComment(@PathVariable("commentId") Long commentId){
        return commentService.deleteComment(commentId);
    }
}
