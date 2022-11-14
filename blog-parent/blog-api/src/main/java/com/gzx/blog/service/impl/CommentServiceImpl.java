package com.gzx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gzx.blog.dao.CommentMapper;
import com.gzx.blog.pojo.Comment;
import com.gzx.blog.pojo.SysUser;
import com.gzx.blog.service.CommentService;
import com.gzx.blog.service.SysUserService;
import com.gzx.blog.utils.UserThreadLocal;
import com.gzx.blog.vo.CommentParam;
import com.gzx.blog.vo.CommentVo;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SysUserService sysUserService;
    @Override
    public Result commentsByArticleId(Long id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getArticleId,id);
        lambdaQueryWrapper.eq(Comment::getLevel,1);
        lambdaQueryWrapper.orderByDesc(Comment::getCreateDate);
        List<Comment> list = commentMapper.selectList(lambdaQueryWrapper);

        List<CommentVo> voList=copyList(list);


        return Result.success(voList);
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser = UserThreadLocal.get();
        Comment comment=new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent=commentParam.getParent();
        if(parent==null || parent==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent==null?0:parent);
        Long toUser=commentParam.getToUserId();
        comment.setToUid(toUser==null?0:toUser);
        this.commentMapper.insert(comment);

        return Result.success(null);
    }

    private List<CommentVo> copyList(List<Comment> list) {
        List<CommentVo> commentVos=new ArrayList<>();
        for (Comment comment : list) {
            commentVos.add(copy(comment));
        }

        return commentVos;
    }

    private CommentVo copy(Comment comment) {
        CommentVo commentVo=new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);

        //取出作者信息
        UserVo userVo=new UserVo();
        Long authorId = comment.getAuthorId();
        SysUser userById = sysUserService.findUserById(authorId);

        userVo.setId(authorId);
        userVo.setNickname(userById.getNickname());
        userVo.setAvatar(userById.getAvatar());

        commentVo.setAuthor(userVo);

        //取出子评论
        Integer level = comment.getLevel();
        if (level==1){
//            Long parentId = comment.getParentId();
            Long id = comment.getId();
            LambdaQueryWrapper<Comment> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Comment::getParentId,id);
            List<Comment> list = commentMapper.selectList(lambdaQueryWrapper);
            List<CommentVo> commentVos = copyList(list);
            commentVo.setChildrens(commentVos);
        }

        //给谁评论（只有子评论才有）
        if (level>1){
            Long parentId1 = comment.getParentId();
            UserVo userVo1=new UserVo();
            Comment comment1 = commentMapper.selectById(parentId1);
            SysUser userById1 = sysUserService.findUserById(comment1.getAuthorId());

            userVo1.setId(userById1.getId());
            userVo1.setNickname(userById1.getNickname());
            userVo1.setAvatar(userById1.getAvatar());
            commentVo.setToUser(userVo1);
        }

        return commentVo;

    }
}
