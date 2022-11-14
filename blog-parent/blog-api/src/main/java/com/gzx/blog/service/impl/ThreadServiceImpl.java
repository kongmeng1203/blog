package com.gzx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gzx.blog.dao.ArticleMapper;
import com.gzx.blog.pojo.Article;
import com.gzx.blog.service.ThreadService;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ThreadServiceImpl implements ThreadService {


    @Async("taskExecutor")
    @Override
    public void updateArticleViewCount(ArticleMapper articleMapper, Article article) {

            int viewCounts = article.getViewCounts();
            Article updateArticle = new Article();
            updateArticle.setViewCounts(viewCounts+1)   ;



            LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Article::getId,article.getId());
            lambdaQueryWrapper.eq(Article::getViewCounts,viewCounts);
            articleMapper.update(updateArticle,lambdaQueryWrapper);


            System.out.println("更新完成");

    }
}
