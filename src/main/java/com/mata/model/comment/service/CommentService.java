package com.mata.model.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.comment.dto.CommentDto;
import com.mata.model.comment.vo.CommentVo;
import com.mata.pojo.Comment;

public interface CommentService extends IService<Comment> {
    /**
     * 评论商品/文章/某个评论
     */
    Result comment(CommentDto commentDto);

    /**
     * 获取评论 根据从属id
     */
    Result<PageResult<CommentVo>> getComment(Long targetId, Integer page);

    /**
     * 点赞某个评论
     */
    Result toGoodComment(Long commentId);

    /**
     * 取消点赞
     */
    Result deleteGoodComment(Long commentId);


    /**
     * 删除评论
     */
    Result deleteComment(Long commentId);
}
