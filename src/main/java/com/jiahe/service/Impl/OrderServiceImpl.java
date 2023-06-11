package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiahe.dao.*;
import com.jiahe.dto.DelShopCartDto;
import com.jiahe.dto.OrderDto;
import com.jiahe.dto.OrderCommodityDto;
import com.jiahe.dto.ShoppingCartDto;
import com.jiahe.pojo.*;
import com.jiahe.service.CommodityService;
import com.jiahe.service.OrderCommodityService;
import com.jiahe.service.OrderService;
import com.jiahe.service.UsersService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    private UsersService usersService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private OrderCommodityService orderCommodityService;

    @Override
    public Boolean addShoppingCart(Integer commodityId, Integer userId, Integer count) {
        // 该用户是否已经添加过该商品
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        wrapper.eq(ShoppingCart::getCommodityId,commodityId);
        ShoppingCart shoppingCart = shoppingCartDao.selectOne(wrapper);
        if (shoppingCart != null){
            // 已经添加过该商品，更新数量
            shoppingCart.setCount(count);
            shoppingCartDao.updateById(shoppingCart);
            return true;
        }
        ShoppingCart shoppingCartItem = new ShoppingCart();
        shoppingCartItem.setCommodityId(commodityId);
        shoppingCartItem.setUserId(userId);
        shoppingCartItem.setCount(count);

        shoppingCartDao.insert(shoppingCartItem);
        return true;
    }

    @Override
    public Boolean updateShoppingCart(Integer id, Integer count) {
        ShoppingCart shoppingCart = shoppingCartDao.selectById(id);
        shoppingCart.setCount(count);
        shoppingCartDao.updateById(shoppingCart);
        return true;
    }

    @Override
    public Boolean deleteShoppingCart(DelShopCartDto shoppingCartIds) {
        for (Integer shoppingCartId : shoppingCartIds.getShoppingCartIds()) {
            shoppingCartDao.deleteById(shoppingCartId);
        }
        return true;
    }

    @Override
    public List<ShoppingCartDto> selectShoppingCart(Integer userId) {
        // 根据用户id查询购物车列表，再根据商品id查询商品信息
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartDao.selectList(wrapper);
        // 遍历购物车列表，查询商品信息
        List<ShoppingCartDto> shoppingCartDtos = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCarts) {
            LambdaQueryWrapper<Commodity> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(Commodity::getId,shoppingCart.getCommodityId());
            Commodity commodity1 = commodityDao.selectOne(wrapper1);
            // 将商品信息复制到购物车中
            ShoppingCartDto shoppingCartDto = new ShoppingCartDto();
            shoppingCartDto.setBrandName(commodity1.getBrandName());
            shoppingCartDto.setCommodityId(commodity1.getId());
            shoppingCartDto.setCommodityName(commodity1.getCommodityName());
            shoppingCartDto.setImage(commodity1.getImage());
            shoppingCartDto.setPrice(commodity1.getPrice());
            shoppingCartDto.setDiscount(commodity1.getDiscount());
            shoppingCartDto.setSize(commodity1.getSize());
            shoppingCartDto.setCount(shoppingCart.getCount());
            shoppingCartDto.setUserId(shoppingCart.getUserId());
            shoppingCartDto.setId(shoppingCart.getId());
            shoppingCartDtos.add(shoppingCartDto);
        }
        System.out.println("最终结果 = " + shoppingCartDtos);
        return shoppingCartDtos;
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
            orderDto.setUsername(user.getName());
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
            orderCommodityDto.setBrandName(commodity.getBrandName());
            orderCommodityDto.setImage(commodity.getImage());
            orderCommodityDto.setSize(commodity.getSize());
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
    public Integer insertOrderCommodities(Integer userId, Integer addressId, OrderCommodity[] orderCommodities) {
        // 创建订单且返回订单id
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressId(addressId);
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
        BigDecimal totalOriginalPrice = new BigDecimal(0);
        for (OrderCommodity orderCommodity : orderCommodities) {
            totalNum += orderCommodity.getCount();
            totalPrice = totalPrice.add(orderCommodity.getPrice().multiply(new BigDecimal(orderCommodity.getCount())));
            totalOriginalPrice = totalOriginalPrice.add(orderCommodity.getOriginalPrice().multiply(new BigDecimal(orderCommodity.getCount())));
        }
        order.setSum(totalNum);
        order.setPrice(totalPrice);
        order.setOriginalPrice(totalOriginalPrice);
        orderDao.updateById(order);
        return orderId;
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

    //处理售后订单对应的状态，将订单状态为4(退货中)的订单改5(已经处理)
    @Override
    public Boolean updateOrderForAftermarket(Integer orderId,Integer status) {
        Order order = orderDao.selectById(orderId);
        order.setStatus(status);
        return orderDao.updateById(order) > 0;
    }

    //将订单对应的订单项的状态也处理一下，改为2,1为退款中
    @Override
    public Boolean updateOrderDetailForAftermarket(Integer orderCommodityId,Integer status) {
        LambdaQueryWrapper<OrderCommodity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderCommodity::getId,orderCommodityId);
        OrderCommodity orderCommodity = orderCommodityDao.selectOne(wrapper);
        if (orderCommodity == null){
            return false;
        }
        orderCommodity.setStatus(status);
        return orderCommodityDao.updateById(orderCommodity) > 0;
    }

    @Override
    @Transactional
    public Integer submitOrder(OrderDto order) {
        Users users = usersService.searchUser(order.getUserId());
        order.setUsername(users.getName());
        List<OrderCommodityDto> orderCommodityList = order.getOrderCommodityList();
        BigDecimal originalPrice = new BigDecimal(0); //计算订单原价
        BigDecimal price = new BigDecimal(0); //计算订单的优惠价
        for(OrderCommodityDto orderCommodityDto:orderCommodityList){
            orderCommodityDto.setStatus(0);
            Commodity commodity = commodityService.getById(orderCommodityDto.getCommodityId());
            //判断商品的存库够不够这次购买数
            Integer remnant = commodity.getRemnant();
            Integer num = orderCommodityDto.getCount();
            if(remnant >= num){
                //可以购买，库存足够，需要减去库存
                /*if(remnant - num == 0){
                    commodity.setStatus(1);
                }*/
                commodity.setRemnant(remnant - num);
                //更新库存，进行购买
                commodityService.updateById(commodity);
            }else {
                //只要有一个库存不足就返回
                //直接发回错误码
                System.out.println("hello");
                return 2;
            }
            orderCommodityDto.setCommodityName(commodity.getCommodityName());
            price=price.add(orderCommodityDto.getPriceSum());
            originalPrice=originalPrice.add(orderCommodityDto.getOriginalPrice().multiply(new BigDecimal(orderCommodityDto.getCount())));
            Integer count = order.getCount();
            order.setCount(count+1);
            Integer sum = order.getSum();
            order.setSum(sum+orderCommodityDto.getCount());
        }
        order.setOriginalPrice(originalPrice);
        order.setPrice(price);
        boolean flag = orderService.save(order);
        if (flag){
            for(OrderCommodityDto orderCommodityDto:orderCommodityList){
                orderCommodityDto.setOrderId(order.getId());
                boolean flag1 = orderCommodityService.save(orderCommodityDto);
                if (flag1 != true) {
                    return 1;
                }
            }
        }
        return 0;
    }

}
