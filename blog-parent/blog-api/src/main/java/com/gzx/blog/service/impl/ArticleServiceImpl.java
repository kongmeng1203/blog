package com.gzx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gzx.blog.dao.ArticleBodyMapper;
import com.gzx.blog.dao.ArticleMapper;
import com.gzx.blog.dao.ArticleTagMapper;
import com.gzx.blog.dos.Archives;
import com.gzx.blog.pojo.Article;
import com.gzx.blog.pojo.ArticleBody;
import com.gzx.blog.pojo.ArticleTag;
import com.gzx.blog.pojo.SysUser;
import com.gzx.blog.service.*;
import com.gzx.blog.utils.UserThreadLocal;
import com.gzx.blog.vo.ArticleBodyVo;
import com.gzx.blog.vo.ArticleVo;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.TagVo;
import com.gzx.blog.vo.params.ArticleParam;
import com.gzx.blog.vo.params.PageParams;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    //加载文章
//    @Override
//    public Result listArticle(PageParams params) {
//
//        IPage<Article> page = new Page<>(params.getPage(),params.getPageSize());
//        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
//        if (params.getCategoryId() != null){
//            lambdaQueryWrapper.eq(Article::getCategoryId,params.getCategoryId());
//        }
//
//        List<Long> articleIdList=new ArrayList<>();
//        if (params.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> tagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            tagLambdaQueryWrapper.eq(ArticleTag::getTagId,params.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(tagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size()>0){
//                lambdaQueryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//        //order by create_date desc
//        lambdaQueryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//
//        articleMapper.selectPage(page,lambdaQueryWrapper);
//        List<Article> records = page.getRecords();
//        List<ArticleVo> articleVoList = copyList(records,true,true);
//        return Result.success(articleVoList);
//    }

    @Override
    public Result listArticle(PageParams params) {
        IPage<Article> page = new Page<>(params.getPage(),params.getPageSize());
        IPage<Article> articleIPage = articleMapper.listArticle(page, params.getCategoryId(), params.getTagId(), params.getYear(), params.getMonth());
        List<Article> records = articleIPage.getRecords();
        return Result.success(copyList(records,true,true));
    }

    //加载最热文章
    @Override
    public Result hotArticle(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Article::getViewCounts);
        lambdaQueryWrapper.select(Article::getId,Article::getTitle);
        lambdaQueryWrapper.last("limit "+limit);
        List<Article> list = articleMapper.selectList(lambdaQueryWrapper);
        return Result.success(copyList(list,false,false));
    }
    //加载最新文章
    @Override
    public Result newArticle(int limit) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Article::getCreateDate);
        lambdaQueryWrapper.select(Article::getId,Article::getTitle);
        lambdaQueryWrapper.last("limit "+limit);
        List<Article> list = articleMapper.selectList(lambdaQueryWrapper);

        return Result.success(copyList(list,false,false));
    }

    //文章归档功能实现
    @Override
    public Result listArchives() {
        List<Archives>  archivesList= articleMapper.listArchives();
        return Result.success(archivesList);
    }

    @Autowired
    private ThreadService threadService;
    //加载文章详情
    @Override
    public Result findArticleById(Long articleId) {
        Article article=articleMapper.selectById(articleId);
        ArticleVo copy = copy(article, true, true,true,true);
        threadService.updateArticleViewCount(articleMapper,article);
        return Result.success(copy);
    }

    //发布文章
    @Override
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();
        Article article = new Article();
        article.setWeight(Article.Article_Common);
        article.setViewCounts(0);
        article.setTitle(articleParam.getTitle());
        article.setSummary(articleParam.getSummary());
        article.setCommentCounts(0);
        article.setCategoryId(articleParam.getCategory().getId());
        System.out.println("sys对象: "+sysUser);
        article.setAuthorId(sysUser.getId());

        article.setCreateDate(System.currentTimeMillis());
        articleMapper.insert(article);
        List<TagVo> tags = articleParam.getTags();
        if (tags != null){
            for (TagVo tag : tags) {
                Long articleId = article.getId();
                ArticleTag articleTag = new ArticleTag();
                articleTag.setTagId(tag.getId());
                articleTag.setArticleId(articleId);
                articleTagMapper.insert(articleTag);
            }

        }

        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBodyMapper.insert(articleBody);
        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);

        Map<String,String> map = new HashMap<>();
        map.put("id",article.getId().toString());



        return Result.success(map);
    }

    private List<ArticleVo> copyList(List<Article> records,boolean isTag ,boolean iaAutor) {
        List<ArticleVo> list=new ArrayList<>();
        for (Article record : records) {
            ArticleVo copy = copy(record,isTag,iaAutor,false,false );
            list.add(copy);
        }
        return list;
    }
    private List<ArticleVo> copyList(List<Article> records,boolean isTag ,boolean iaAutor,boolean isBody,boolean isCategory) {
        List<ArticleVo> list=new ArrayList<>();
        for (Article record : records) {
            ArticleVo copy = copy(record,isTag,iaAutor,isBody,isCategory);
            list.add(copy);
        }
        return list;
    }

    private ArticleVo copy(Article article,boolean isTag ,boolean iaAutor,boolean isBody,boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        /**
         * BeanUtils.copyProperties(a, b);
         *
         *     b中的存在的属性，a中一定要有，但是a中可以有多余的属性；
         *     a中与b中相同的属性都会被替换，不管是否有值；
         *     a、 b中的属性要名字相同，才能被赋值，不然的话需要手动赋值；
         *     Spring的BeanUtils的CopyProperties方法需要对应的属性有getter和setter方法；
         *     如果存在属性完全相同的内部类，但是不是同一个内部类，即分别属于各自的内部类，则spring会认为属性不同，不会copy；
         *     spring和apache的copy属性的方法源和目的参数的位置正好相反，所以导包和调用的时候都要注意一下。
         */
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-mm-dd HH:mm"));

        if(isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagService.findTagsByArticleId(articleId));
        }
        if(iaAutor){

            Long authorId = article.getAuthorId();
            articleVo.setAuthor(sysUserService.findUserById(authorId).getNickname());
        }
        if (isBody){
            Long bodyId = article.getBodyId();
            articleVo.setBody(findArticleBodyById(bodyId));
        }
        if (isCategory){
            Long categoryId = article.getCategoryId();
            articleVo.setCategory(categoryService.findCategoryById(categoryId));
        }

        return articleVo;
    }

    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    private ArticleBodyVo findArticleBodyById(Long bodyId) {
        ArticleBody articleBody = articleBodyMapper.selectById(bodyId);
        ArticleBodyVo articleBodyVo=new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

}
