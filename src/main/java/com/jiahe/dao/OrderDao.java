package com.jiahe.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiahe.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

//mybatisPlus
@Mapper
@Repository
public interface OrderDao extends BaseMapper<Order> {
}
