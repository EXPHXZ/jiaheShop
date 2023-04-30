package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.dto.OrderDto;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.OrderCommodity;

public interface OrderService {

//    /**
//     * 根据传入的页码和页数来进行分页查询
//     * @param current
//     * @param pageSize
//     * @return
//     */
//    public IPage<Order> SelectAll(int current, int pageSize);
//
//    /**
//     * 根据id来进行查询订单
//     * @param id
//     * @return
//     */
//    public Commodity searchOrder(Integer id);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     */
    public OrderDto selectOrderDetail(Integer orderId);

    /**
     * 新增多个商品项
     * @param orderCommodities
     * @return
     */
    public Boolean insertOrderCommodities(Integer orderId, OrderCommodity[] orderCommodities);

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
