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
         *     b????????????????????????a????????????????????????a??????????????????????????????
         *     a??????b?????????????????????????????????????????????????????????
         *     a??? b?????????????????????????????????????????????????????????????????????????????????
         *     Spring???BeanUtils???CopyProperties??????????????????????????????getter???setter?????????
         *     ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????spring??????????????????????????????copy???
         *     spring???apache???copy????????????????????????????????????????????????????????????????????????????????????????????????????????????
         */
        BeanUtils.copyProperties(tag,tagVo);




        return tagVo;
    }
}
