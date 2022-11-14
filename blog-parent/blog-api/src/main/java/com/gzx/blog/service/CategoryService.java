package com.gzx.blog.service;

import com.gzx.blog.vo.CategoryVo;
import com.gzx.blog.vo.Result;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long categoryId);

    Result findAll();

    Result findAllDetail();

    Result findCategoryDetailById(Long id);
}
