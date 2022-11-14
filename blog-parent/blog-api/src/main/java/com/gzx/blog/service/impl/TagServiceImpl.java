package com.gzx.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gzx.blog.dao.TagMapper;
import com.gzx.blog.pojo.Article;
import com.gzx.blog.pojo.Tag;
import com.gzx.blog.service.TagService;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.TagVo;

import javafx.print.Collation;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;
    @Override
    public List<TagVo> findTagsByArticleId(Long articleId) {

        List<Tag> tags=tagMapper.findTagsByArticleId(articleId);

        return copyList(tags);
    }

    @Override
    public Result hots(int limit) {

       List<Long> tagIds = tagMapper.findHotsTagIds(limit);
       if (CollectionUtils.isEmpty(tagIds)){
           return Result.success(Collections.emptyList());
       }

       List<Tag> tags = tagMapper.findTagsByTagIds(tagIds);

        return Result.success(tags);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Tag> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Tag::getId,Tag::getTagName);
        List<Tag> tags = tagMapper.selectList(lambdaQueryWrapper);
        List<TagVo> tagVos = copyList(tags);

        return Result.success(tagVos);
    }

    @Override
    public Result findAllDetail() {
        List<Tag> tags = tagMapper.selectList(null);
        List<TagVo> tagVos = copyList(tags);

        return Result.success(tagVos);
    }

    @Override
    public Result findDetailById(Long id) {
        Tag tag = tagMapper.selectById(id);

        return Result.success(copy(tag));
    }

    private List<TagVo> copyList(List<Tag> records) {
        List<TagVo> list=new ArrayList<>();
        for (Tag record : records) {
            TagVo copy = copy(record);
            list.add(copy);
        }
        return list;
    }

    private TagVo copy(Tag tag){
        TagVo tagVo = new TagVo();
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
        BeanUtils.copyProperties(tag,tagVo);




        return tagVo;
    }
}
