package com.jiahe.service.Impl;

import com.jiahe.dao.OrderCommodityDao;
import com.jiahe.dao.OrderDao;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.OrderCommodity;
import com.jiahe.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCommodityDao orderCommodityDao;

    @Override
    public Boolean insertOrderCommodities(OrderCommodity[] orderCommodities) {
        for (OrderCommodity orderCommodity : orderCommodities) {
            orderCommodityDao.insert(orderCommodity);
        }
        return true;
    }

    @Override
    public Boolean insertOrder(Order order) {
        return orderDao.insert(order) > 0;
    }

    @Override
    public Boolean deleteOrderCommodity(Integer id) {
        return orderCommodityDao.deleteById(id) > 0;
    }

    @Override
    public Boolean deleteOrder(Integer id) {
        return orderDao.deleteById(id) > 0;
    }

    @Override
    public Boolean updateOrderCommodity(OrderCommodity orderCommodity) {
        return orderCommodityDao.updateById(orderCommodity) > 0;
    }

    @Override
    public Boolean updateOrder(Order order) {
        return orderDao.updateById(order) > 0;
    }


}
