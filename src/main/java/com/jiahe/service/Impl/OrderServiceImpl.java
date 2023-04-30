package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.dao.OrderCommodityDao;
import com.jiahe.dao.OrderDao;
import com.jiahe.dao.CommodityDao;
import com.jiahe.dao.UserDao;
import com.jiahe.dto.OrderDto;
import com.jiahe.dto.OrderCommodityDto;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.Order;
import com.jiahe.pojo.OrderCommodity;
import com.jiahe.pojo.User;
import com.jiahe.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCommodityDao orderCommodityDao;
    @Autowired
    private CommodityDao commodityDao;
    @Autowired
    private UserDao userDao;


    @Override
    public OrderDto selectOrderDetail(Integer orderId){
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId,orderId);
        //查询对于的订单信息
        Order order = orderDao.selectOne(wrapper);
        OrderDto orderDto = new OrderDto();
        //复制订单的属性值到dto中
        BeanUtils.copyProperties(order,orderDto);
        System.out.println("这一个是orderDto：   " + orderDto);

        // 查询用户名
        LambdaQueryWrapper<User> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(User::getId,order.getUserId());
        User user = userDao.selectOne(wrapper2);
        orderDto.setUsername(user.getUsername());

        // 查询订单项
        LambdaQueryWrapper<OrderCommodity> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(OrderCommodity::getOrderId,orderId);
        List<OrderCommodity> orderCommodities = orderCommodityDao.selectList(wrapper1);
        List<OrderCommodityDto> orderCommodityDtos = new ArrayList<>();
        for (OrderCommodity orderCommodity : orderCommodities) {
            OrderCommodityDto orderCommodityDto = new OrderCommodityDto();
            BeanUtils.copyProperties(orderCommodity,orderCommodityDto);

            // 查询商品名
            LambdaQueryWrapper<Commodity> wrapper3 = new LambdaQueryWrapper<>();
            wrapper3.eq(Commodity::getId,orderCommodity.getCommodityId());
            Commodity commodity = commodityDao.selectOne(wrapper3);
            orderCommodityDtos.add(orderCommodityDto);
        }
        orderDto.setOrderCommodityList(orderCommodityDtos);

        return orderDto;
    }

    @Override
    public Boolean insertOrderCommodities(Integer orderId, OrderCommodity[] orderCommodities) {
        for (OrderCommodity orderCommodity : orderCommodities) {
            orderCommodity.setOrderId(orderId);
            orderCommodity.setPriceSum(orderCommodity.getPrice().multiply(new BigDecimal(orderCommodity.getCount())));
            orderCommodityDao.insert(orderCommodity);
        }
        // 计算订单项总数、购买数量、总价
        Order order = new Order();
        order.setId(orderId);
        order.setCount(orderCommodities.length);
        Integer totalNum = 0;
        BigDecimal totalPrice = new BigDecimal(0);
        for (OrderCommodity orderCommodity : orderCommodities) {
            totalNum += orderCommodity.getCount();
            totalPrice = totalPrice.add(orderCommodity.getPrice().multiply(new BigDecimal(orderCommodity.getCount())));
        }
        order.setSum(totalNum);
        order.setPrice(totalPrice);
        orderDao.updateById(order);
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
