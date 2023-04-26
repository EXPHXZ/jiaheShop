package com.jiahe.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiahe.pojo.Commodity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommodityDao extends BaseMapper<Commodity> {
}
