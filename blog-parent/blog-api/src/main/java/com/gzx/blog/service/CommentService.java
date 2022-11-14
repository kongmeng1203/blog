package com.gzx.blog.service;

import com.gzx.blog.vo.CommentParam;
import com.gzx.blog.vo.Result;

public interface CommentService {
    Result commentsByArticleId(Long id);

    Result comment(CommentParam commentParam);
}
