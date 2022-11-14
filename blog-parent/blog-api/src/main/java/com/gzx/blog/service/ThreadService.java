package com.gzx.blog.service;

import com.gzx.blog.dao.ArticleMapper;
import com.gzx.blog.pojo.Article;

public interface ThreadService {
    void updateArticleViewCount(ArticleMapper articleMapper, Article article);
}
