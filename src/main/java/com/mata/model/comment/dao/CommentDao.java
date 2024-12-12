package com.mata.model.comment.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mata.model.comment.vo.CommentVo;
import com.mata.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {
    /**
     * 获取评论 根据从属id
     */
    IPage<CommentVo> getComment(IPage<CommentVo> page,@Param("targetId") Long targetId);
}
