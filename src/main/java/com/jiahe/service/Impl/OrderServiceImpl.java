package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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
    public IPage<OrderDto> selectAllOrder(Integer page, Integer size, Integer desc) {
        // 查询所有订单
        IPage<Order> orderIPage = orderDao.selectPage(new Page<>(page, size), null);
        // 判空
        if (orderIPage == null){
            return null;
        }

        // 将订单转换为dto
        Page<OrderDto> orderDtoIPage = new Page<>(page, size);
        List<Order> orders = orderIPage.getRecords();
        List<OrderDto> orderDtos = new ArrayList<>();

        Collections.reverse(orders);

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();

            // 复制订单的属性值到dto中
            BeanUtils.copyProperties(order,orderDto);

            // 查询用户名
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getId,order.getUserId());
            User user = userDao.selectOne(wrapper);
            orderDto.setUsername(user.getUsername());

            orderDtos.add(orderDto);
        }
        // 将长度放入dto中
        orderDtoIPage.setTotal(orderIPage.getTotal());
        orderDtoIPage.setRecords(orderDtos);
        return orderDtoIPage;
    }

    @Override
    public OrderDto selectOrderDetail(Integer orderId){

        // 查询订单基本信息
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId,orderId);
        Order order = orderDao.selectOne(wrapper);
        // 判空
        if (order == null){
            return null;
        }
        OrderDto orderDto = new OrderDto();

        // 复制订单的属性值到dto中
        BeanUtils.copyProperties(order,orderDto);

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
            orderCommodityDto.setCommodityName(commodity.getCommodityName());
            orderCommodityDtos.add(orderCommodityDto);
        }
        orderDto.setOrderCommodityList(orderCommodityDtos);

        return orderDto;
    }

    @Override
    public IPage<OrderDto> selectOrderByUserName(String username, Integer page, Integer size, Integer desc) {
        // 根据userName在user表中模糊查询，查询对应的所有用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(User::getUsername,username);
        List<User> users = userDao.selectList(wrapper);

        // 判空
        if (users.isEmpty()){
            return null;
        }

        // 将用户id放入list中
        List<Integer> userIds = new ArrayList<>();
        for (User user : users) {
            userIds.add(user.getId());
        }

        // 根据用户id在order表中查询订单
        IPage<Order> orderIPage = orderDao.selectPage(new Page<>(page, size), new LambdaQueryWrapper<Order>().in(Order::getUserId, userIds));

        // 判空
        if (orderIPage == null){
            return null;
        }

        // 将订单转换为dto
        Page<OrderDto> orderDtoIPage = new Page<>(page, size);
        List<Order> orders = orderIPage.getRecords();
        List<OrderDto> orderDtos = new ArrayList<>();

        Collections.reverse(orders);

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order,orderDto);

            // 查询用户名
            LambdaQueryWrapper<User> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(User::getId,order.getUserId());
            User user = userDao.selectOne(wrapper1);
            orderDto.setUsername(user.getUsername());

            orderDtos.add(orderDto);
        }
        // 将长度放入dto中
        orderDtoIPage.setTotal(orderIPage.getTotal());
        orderDtoIPage.setRecords(orderDtos);
        return orderDtoIPage;
    }

    @Override
    public Boolean insertOrderCommodities(Integer userId, OrderCommodity[] orderCommodities) {
        // 创建订单且返回订单id
        Order order = new Order();
        order.setUserId(userId);
        orderDao.insert(order);
        Integer orderId = order.getId();

        for (OrderCommodity orderCommodity : orderCommodities) {
            orderCommodity.setOrderId(orderId);
            orderCommodity.setPriceSum(orderCommodity.getPrice().multiply(new BigDecimal(orderCommodity.getCount())));
            orderCommodityDao.insert(orderCommodity);
        }

        // 计算订单项总数、购买数量、总价
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
    public Boolean deleteOrder(Integer id) {
        return orderDao.deleteById(id) > 0;
    }

    @Override
    public Boolean updateOrderCommodity(@RequestParam Integer orderId, OrderCommodity orderCommodity) {

        // 将orderId对应的订单状态改为1
        Order order = new Order();
        order.setId(orderId);
        order.setStatus(1);
        orderDao.updateById(order);

        return orderCommodityDao.updateById(orderCommodity) > 0;
    }


    //售后模块用的order相关办法
    //查询售后orderId所对应的订单信息
    @Override
    public Order searchOrder(Integer orderId) {
        Order order = orderDao.selectById(orderId);
        return order;
    }

    //处理售后订单对应的状态，将订单状态为1(退货中)的订单改为0(正常)
    @Override
    public Boolean updateOrderForAftermarket(Order order) {
        order.setStatus(0);
        return orderDao.updateById(order) > 0;
    }

    //将订单对应的订单项的状态也处理一下，改为2
    @Override
    public Boolean updateOrderDetailForAftermarket(Integer orderId) {
        LambdaQueryWrapper<OrderCommodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderCommodity::getOrderId,orderId);
        wrapper.eq(OrderCommodity::getStatus,1);
        List<OrderCommodity> orderCommodities = orderCommodityDao.selectList(wrapper);

        for(OrderCommodity orderCommodity:orderCommodities){
            orderCommodity.setStatus(2);
            orderCommodityDao.updateById(orderCommodity);
        }

        return true;
    }



}
