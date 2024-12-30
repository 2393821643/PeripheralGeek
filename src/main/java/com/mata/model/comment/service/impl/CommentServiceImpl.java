package com.mata.model.comment.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mata.common.result.PageResult;
import com.mata.common.result.Result;
import com.mata.model.comment.dao.CommentDao;
import com.mata.model.comment.dao.CommentGoodDao;
import com.mata.model.comment.dto.CommentDto;
import com.mata.model.comment.service.CommentService;
import com.mata.model.comment.vo.CommentVo;
import com.mata.pojo.Comment;
import com.mata.pojo.CommentGood;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentDao, Comment> implements CommentService {
    @Autowired
    @Qualifier("commentGoodCountBloom")
    private RBloomFilter  commentGoodCountBloom;

    @Autowired
    private CommentGoodDao commentGoodDao;

    /**
     * 评论商品/文章/某个评论
     */
    @Override
    public Result<String> comment(CommentDto commentDto) {
        Comment comment = BeanUtil.copyProperties(commentDto, Comment.class);
        comment.setUserId(StpUtil.getLoginIdAsInt());
        comment.setCreateTime(LocalDateTime.now());
        comment.setCommentId(IdUtil.getSnowflakeNextId());
        comment.setGoodCount(0);
        save(comment);
        return Result.success(comment.getCommentId().toString(),"评论成功");
    }

    /**
     * 获取评论 根据从属id
     */
    @Override
    public Result<PageResult<CommentVo>> getComment(Long targetId, Integer page) {
        // 当前页 一页20个
        Page<CommentVo> pageConfig = new Page<>(page, 20);
        IPage<CommentVo> commentPage = baseMapper.getComment(pageConfig, targetId);
        List<CommentVo> commentList = commentPage.getRecords();
        // 如果当前登录 则判断此评论是否被点赞
        try {
            String userId = StpUtil.getLoginIdAsString();
            List<String> isGoodCommentIdList = commentList.stream()
                    .filter(commentVo -> commentGoodCountBloom.contains(userId + commentVo.getCommentId().toString()))
                    .map(commentVo -> userId + commentVo.getCommentId().toString())
                    .collect(Collectors.toList());
            // 设置布隆过滤器不存在的评论为 false
            commentList.stream()
                    .filter(commentVo -> !commentGoodCountBloom.contains(userId + commentVo.getCommentId().toString()))
                    .forEach(commentVo -> commentVo.setIsGood(false));
            // 存储布隆过滤器存在的id 不为空，查数据库
            if (!isGoodCommentIdList.isEmpty()) {
                List<CommentGood> commentGoods = commentGoodDao.selectBatchIds(isGoodCommentIdList);
                Map<String, Boolean> commentGoodMap = commentGoods.stream()
                        .collect(Collectors.toMap(
                                commentGood -> commentGood.getCommentId().toString(),
                                commentGood -> true
                        ));
                commentList.forEach(commentVo -> {
                    if (commentGoodMap.containsKey(commentVo.getCommentId().toString())) {
                        commentVo.setIsGood(true);
                    }
                });
            }
        }catch (NotLoginException e){
            return Result.success(new PageResult<CommentVo>(commentPage.getTotal(),commentList));
        }
        return Result.success(new PageResult<CommentVo>(commentPage.getTotal(),commentList));
    }

    /**
     * 点赞某个评论
     */
    @Override
    public Result toGoodComment(Long commentId) {
        Comment comment = getById(commentId);
        if (ObjectUtil.isEmpty(comment)){
            return Result.error("此评论不存在");
        }
        String userId = StpUtil.getLoginIdAsString();
        commentGoodCountBloom.add(userId + commentId.toString());
        // 生成点赞记录
        CommentGood commentGood = CommentGood.builder()
                .id(userId + commentId.toString())
                .commentId(commentId)
                .userId(Long.valueOf(userId))
                .targetId(comment.getTargetId())
                .build();
        commentGoodDao.insert(commentGood);
        // 修改点赞数
        Comment rusultComment = getById(commentId);
        rusultComment.setGoodCount(rusultComment.getGoodCount()+1);
        updateById(rusultComment);
        return Result.success("点赞成功");
    }

    /**
     * 取消点赞
     */
    @Override
    public Result deleteGoodComment(Long commentId) {
        String userId = StpUtil.getLoginIdAsString();
        commentGoodDao.deleteById(userId + commentId.toString());
        // 修改点赞数
        Comment rusultComment = getById(commentId);
        rusultComment.setGoodCount(rusultComment.getGoodCount()-1);
        updateById(rusultComment);
        return Result.success("取消点赞成功");
    }

    /**
     * 删除评论
     */
    @Override
    @Transactional
    public Result deleteComment(Long commentId) {
        if (StpUtil.hasRole("admin")){
            deleteAllAboutComment(commentId);
        }else if (StpUtil.hasRole("user")){
            // 判断此评论的用户是不是此账号
            int userId = StpUtil.getLoginIdAsInt();
            Comment comment = getById(commentId);
            if (comment.getUserId() == userId) {
                deleteAllAboutComment(commentId);
                return Result.success("删除成功");
            }
        }
        return Result.error("删除失败，你没有此权限");
    }

    /**
     *  删除评论
     */
    private void deleteAllAboutComment(Long commentId){
        // 删除评论
        removeById(commentId);
        // 删除从属评论
        LambdaQueryWrapper<Comment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Comment::getTargetId,commentId);
        remove(wrapper);
        // 删除点赞记录
        LambdaQueryWrapper<CommentGood> goodWrapper = new LambdaQueryWrapper<>();
        goodWrapper.eq(CommentGood::getCommentId,commentId);
        commentGoodDao.delete(goodWrapper);
        // 删除从属点赞记录
        LambdaQueryWrapper<CommentGood> goodTargetWrapper = new LambdaQueryWrapper<>();
        goodTargetWrapper.eq(CommentGood::getTargetId,commentId);
        commentGoodDao.delete(goodTargetWrapper);
    }


}
