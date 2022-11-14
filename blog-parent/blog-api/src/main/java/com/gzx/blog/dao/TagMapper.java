package com.gzx.blog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gzx.blog.pojo.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 根据文章ID查询标签列表
     * @param articleId
     * @return
     */
    List<Tag> findTagsByArticleId(Long articleId);

    List<Long> findHotsTagIds(int limit);

    List<Tag> findTagsByTagIds(List<Long> tagIds);
}
