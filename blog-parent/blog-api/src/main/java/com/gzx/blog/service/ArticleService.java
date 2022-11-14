package com.gzx.blog.service;


import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.params.ArticleParam;
import com.gzx.blog.vo.params.PageParams;


public interface ArticleService {
    /**
     * 分页查询文章列表
     * @return
     */
    public Result listArticle(PageParams params);

    Result hotArticle(int limit);

    Result newArticle(int limit);

    Result listArchives();

    Result findArticleById(Long articleId);

    Result publish(ArticleParam articleParam);
}
