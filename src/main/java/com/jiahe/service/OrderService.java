package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.dto.OrderDto;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.OrderCommodity;

public interface OrderService {

    /**
     * 查询所有订单
     * @return
     */
    public IPage<OrderDto> selectAllOrder(Integer page, Integer size, Integer desc);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     */
    public OrderDto selectOrderDetail(Integer orderId);

    /**
     * 根据用户名查询订单
     * @param userName
     * @return
     */
    public IPage<OrderDto> selectOrderByUserName(String userName, Integer page, Integer size, Integer desc);

    /**
     * 新增多个商品项
     * @param orderCommodities
     * @return
     */
    public Boolean insertOrderCommodities(Integer userId, OrderCommodity[] orderCommodities);

    /**
     * 新增订单
     * @param order
     * @return
     */
    public Boolean insertOrder(Order order);

    /**
     * 删除商品项
     * @param id
     * @return
     */
    public Boolean deleteOrderCommodity(Integer id);
    /**
     * 删除订单
     * @param id
     * @return
     */
    public Boolean deleteOrder(Integer id);

    /**
     * 更新商品项
     * @param orderCommodity
     * @return
     */
    public Boolean updateOrderCommodity(OrderCommodity orderCommodity);
    /**
     * 更新订单
     * @param order
     * @return
     */
    public Boolean updateOrder(Order order);
}
