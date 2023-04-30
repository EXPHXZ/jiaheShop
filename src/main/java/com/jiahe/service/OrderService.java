package com.jiahe.service;

import com.jiahe.pojo.Order;

public interface OrderService {

    /**
     * 新增订单
     * @param order
     * @return
     */
    public Boolean addOrder(Order order);

    /**
     * 删除订单
     * @param id
     * @return
     */
    public Boolean deleteOrder(Integer id);

    /**
     * 更新订单
     * @param order
     * @return
     */
    public Boolean updateOrder(Order order);
}
