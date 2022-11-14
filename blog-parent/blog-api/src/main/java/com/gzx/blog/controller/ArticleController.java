package com.gzx.blog.controller;

import com.gzx.blog.common.aop.LogAnnotation;
import com.gzx.blog.dos.Archives;
import com.gzx.blog.pojo.Article;
import com.gzx.blog.service.ArticleService;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.params.ArticleParam;
import com.gzx.blog.vo.params.PageParams;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
    /**
     * 首页文章列表
     * @param params
     * @return
     */
    @PostMapping
    @LogAnnotation(module = "文章",operator = "文章列表")
    public Result listArticle(@RequestBody PageParams params){
        //System.out.println(articleService.listArticle(params));
        return articleService.listArticle(params);
    }

    @PostMapping("hot")
    public Result hotArticle(){
        int limit=5;
        Result list=articleService.hotArticle(limit);
        return list;
    }

    @PostMapping("new")
    public Result newArticle(){
        int limit = 5;
        return articleService.newArticle(limit);
    }

    @PostMapping("listArchives")
    public Result listArchives(){
        Result list=articleService.listArchives();
        return list;
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long articleId){
       return articleService.findArticleById(articleId);
    }

    @PostMapping("publish")
    public Result publish(@RequestBody ArticleParam articleParam){
        return articleService.publish(articleParam);
    }

}
