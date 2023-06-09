package com.jiahe.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiahe.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CategoryDao extends BaseMapper<Category> {
}
