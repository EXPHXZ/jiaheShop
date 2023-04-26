package com.jiahe.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiahe.pojo.Commodity;
import org.apache.ibatis.annotations.Mapper;

//mybatisPlus
@Mapper
public interface CommodityDao extends BaseMapper<Commodity> {
}
