package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.dao.*;
import com.jiahe.dto.OrderDto;
import com.jiahe.dto.OrderCommodityDto;
import com.jiahe.dto.ShoppingCartDto;
import com.jiahe.pojo.*;
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
public class OrderServiceImpl extends ServiceImpl<OrderDao,Order> implements OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderCommodityDao orderCommodityDao;
    @Autowired
    private CommodityDao commodityDao;
    @Autowired
    private UsersDao usersDao;

    @Autowired
    private ShoppingCartDao shoppingCartDao;

    @Override
    public Boolean addShoppingCart(Integer commodityId, Integer userId, Integer count) {
        ShoppingCart shoppingCartItem = new ShoppingCart();
        shoppingCartItem.setCommodityId(commodityId);
        shoppingCartItem.setUserId(userId);
        shoppingCartItem.setCount(count);

        shoppingCartDao.insert(shoppingCartItem);
        return true;
    }

    @Override
    public ShoppingCartDto selectShoppingCart(Integer userId) {
        System.out.println("userId =         " + userId);
        // 根据用户id查询购物车列表，再根据商品id查询商品信息
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartDao.selectList(wrapper);
        System.out.println("shoppingCarts =   " + shoppingCarts);
        // 再根据商品id查询商品信息
        List<Commodity> orderCommodityDtos = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            System.out.println("shoppingCart = " + shoppingCart);
            Commodity orderCommodityDto = new Commodity();
            // 查询商品信息
            Commodity commodity = commodityDao.selectById(shoppingCart.getCommodityId());
            // 复制商品信息到dto中
            BeanUtils.copyProperties(commodity,orderCommodityDto);
            // 复制购物车信息到dto中
            BeanUtils.copyProperties(shoppingCart,orderCommodityDto);
            orderCommodityDtos.add(orderCommodityDto);
            System.out.println("orderCommodityDtos = " + orderCommodityDtos);
        }
        // 将购物车列表封装到dto中
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
        shoppingCartDto.setCommodity(orderCommodityDtos);
        return null;
    }

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

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();

            // 复制订单的属性值到dto中
            BeanUtils.copyProperties(order,orderDto);

            // 查询用户名
            LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Users::getId,order.getUserId());
            Users user = usersDao.selectOne(wrapper);
            orderDto.setUsername(user.getUsername());
            orderDto.setUsername("begonia");

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
        LambdaQueryWrapper<Users> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.eq(Users::getId,order.getUserId());
        Users user = usersDao.selectOne(wrapper2);
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
        LambdaQueryWrapper<Users> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Users::getUsername,username);
        List<Users> users = usersDao.selectList(wrapper);

        // 判空
        if (users.isEmpty()){
            return null;
        }

        // 将用户id放入list中
        List<Integer> userIds = new ArrayList<>();
        for (Users user : users) {
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

        for (Order order : orders) {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order,orderDto);

            // 查询用户名
            LambdaQueryWrapper<Users> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Users::getId,order.getUserId());
            Users user = usersDao.selectOne(wrapper1);
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
    public Boolean updateOrderForAftermarket(Integer orderId) {
        Order order = orderDao.selectById(orderId);
        order.setStatus(2);
        return orderDao.updateById(order) > 0;
    }

    //将订单对应的订单项的状态也处理一下，改为2
    @Override
    public Boolean updateOrderDetailForAftermarket(Integer orderCommodityId) {
        LambdaQueryWrapper<OrderCommodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderCommodity::getId,orderCommodityId);
        wrapper.eq(OrderCommodity::getStatus,1);
        OrderCommodity orderCommodity = orderCommodityDao.selectOne(wrapper);
        if (orderCommodity == null){
            return false;
        }
        orderCommodity.setStatus(2);
        return true;
    }

}
