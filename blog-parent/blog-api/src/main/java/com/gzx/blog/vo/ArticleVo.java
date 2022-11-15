package com.gzx.blog.vo;

import com.alibaba.fastjson.serializer.ToStringSerializer;


import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

import java.util.List;

@Data
public class ArticleVo {
    //@JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String title;

    private String summary;

    private Integer commentCounts;

    private Integer viewCounts;

    private Integer weight;
    /**
     * 创建时间
     */
    private String createDate;

    private String author;

    private ArticleBodyVo body;

    private List<TagVo> tags;

    private CategoryVo category;
}
