package com.gzx.blog.service;

import com.gzx.blog.pojo.Tag;
import com.gzx.blog.vo.Result;
import com.gzx.blog.vo.TagVo;

import java.util.List;

public interface TagService {

    List<TagVo> findTagsByArticleId(Long articleId);

    Result hots(int limit);

    Result findAll();

    Result findAllDetail();

    Result findDetailById(Long id);
}
