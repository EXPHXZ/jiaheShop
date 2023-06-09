package com.jiahe.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dto.DelShopCartDto;
import com.jiahe.dto.OrderCommodityDto;
import com.jiahe.dto.OrderDto;
import com.jiahe.dto.ShoppingCartDto;
import com.jiahe.pojo.*;
import com.jiahe.service.*;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AftermarketService aftermarketService;

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private OrderCommodityService orderCommodityService;

    @Autowired
    private AddressService addressService;

    /**
     * 将商品加入购物车
     * @param commodityId
     * @param userId
     * @param count
     * @return
     */
    @PostMapping("/addShoppingCart")
    public Result addShoppingCart(@RequestParam Integer commodityId, @RequestParam Integer userId, @RequestParam Integer count){
        Boolean flag = orderService.addShoppingCart(commodityId, userId, count);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"添加成功":"添加失败");
    }

    /**
     * 更新购物车商品数量
     * @param id
     * @param count
     * @return
     */
    @PostMapping("/updateShoppingCart")
    public Result updateShoppingCart(@RequestParam Integer id, @RequestParam Integer count){
        Boolean flag = orderService.updateShoppingCart(id, count);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"更新成功":"更新失败");
    }

    /**
     * 删除购物车商品，传入购物车id数组
     * @param shoppingCartIds
     * @return
     */
    @PostMapping("/deleteShoppingCart")
    public Result deleteShoppingCart(@RequestBody DelShopCartDto shoppingCartIds){
        Boolean flag = orderService.deleteShoppingCart(shoppingCartIds);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

    /**
     * 查询购物车
     * @param userId
     * @return
     */
    @GetMapping("/selectShoppingCart")
    public Result selectShoppingCart(@RequestParam Integer userId){
        List<ShoppingCartDto> data = orderService.selectShoppingCart(userId);
        System.out.println(data);
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"查询失败");
    }

    /**
     * 查询所有订单
     * @param page
     * @param size
     * @param desc
     * @return
     */
    @GetMapping("/selectAllOrder")
    public Result selectAllOrder(@RequestParam Integer page, @RequestParam Integer size, Integer desc){
        IPage<OrderDto> data = orderService.selectAllOrder(page, size, desc);
        System.out.println(data + "--------------------------------");
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"查询失败");
    }

    /**
     * 根据id查询订单详情
     * @param id
     */
    @GetMapping("/selectOrderDetailById")
    public Result selectOrderDetail(@RequestParam Integer id){
        OrderDto data = orderService.selectOrderDetail(id);
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"订单不存在");
    }

    /**
     * 根据用户名查询订单
     * @param userName
     * @param desc
     * @return
     */
    @GetMapping("/selectOrderByUserName")
    public Result selectOrderByUserName(@RequestParam String userName, @RequestParam Integer page, @RequestParam Integer size, @RequestParam Integer desc){
        IPage<OrderDto> data = orderService.selectOrderByUserName(userName, page, size, desc);
        Boolean flag = data != null;
        return new Result(data,flag? Code.SELECT_SUCCESS:Code.SELECT_FAIL,flag?"查询成功":"查询失败");
    }

    /**
     * 根据id删除订单
     * @param id
     */
    @PostMapping("/deleteOrder")
    public Result deleteOrder(@RequestParam Integer id){
        Boolean flag = orderService.deleteOrder(id);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

    /**
     * 新增订单
     * 使用Json的方式传递参数，订单项对象数组
     * @param orderCommodities
     */
    @PostMapping("/insertOrderCommodity")
    public Result insertOrderCommodity(@RequestBody OrderCommodity[] orderCommodities, @RequestParam Integer userId, @RequestParam Integer addressId){
        Integer flag = orderService.insertOrderCommodities(addressId, userId, orderCommodities);
        return new Result(flag,flag != null? Code.ADD_SUCCESS:Code.ADD_FAIL,flag != null?"添加成功":"添加失败");
    }


    /*
    分隔，前台和后台分界线
     */

    /**
     * 前台的购买业务
     * @param order
     * @return
     */


    @PostMapping("/submitOrder")
    public Result submitOrder(@RequestBody OrderDto order){
        System.out.println(order);
        Users users = usersService.searchUser(order.getUserId());
        order.setUsername(users.getName());
        List<OrderCommodityDto> orderCommodityList = order.getOrderCommodityList();
        BigDecimal originalPrice = new BigDecimal(0); //计算订单原价
        BigDecimal price = new BigDecimal(0); //计算订单的优惠价
        for(OrderCommodityDto orderCommodityDto:orderCommodityList){
            orderCommodityDto.setStatus(0);
            Commodity commodity = commodityService.getById(orderCommodityDto.getCommodityId());
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
                    return new Result(null,Code.ADD_FAIL,"购买失败");
                }
            }
        }
        return new Result(order.getId(),Code.ADD_SUCCESS,"购买成功");
    }

    @GetMapping("/search/{current}/{pageSize}/{userId}/{status}")
    public Result getAllOrder(@PathVariable Integer current,@PathVariable Integer pageSize,@PathVariable Integer userId,@PathVariable Integer status){
        //先查询当前用户下的订单
        Page<Order> orderPage = new Page<>(current,pageSize);
        Page<OrderDto> orderDtoPage = new Page<>(current,pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId,userId);
        if (status > 3){
            return new Result(null,Code.SELECT_FAIL,"查询失败");
        } else if (status == 3){
            wrapper.eq(Order::getStatus,3).or().eq(Order::getStatus,4);
        }else if (status >= 0 && status < 3){
            wrapper.eq(Order::getStatus,status);
        }
        orderService.page(orderPage, wrapper);
        BeanUtils.copyProperties(orderPage,orderDtoPage,"records");
        List<Order> records = orderPage.getRecords();
        List<OrderDto> orderDtos = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            Integer userId1 = item.getUserId();
            Users users = usersService.searchUser(userId1);
            orderDto.setUsername(users.getName());
            Integer addressId = item.getAddressId();
            Address address = addressService.searchAddress(addressId);
            orderDto.setAddress(address);
            Integer id = item.getId();
            LambdaQueryWrapper<OrderCommodity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderCommodity::getOrderId, id);
            List<OrderCommodity> orderCommodities = orderCommodityService.list(queryWrapper);
            List<OrderCommodityDto> orderCommodityDtos = new ArrayList<>();
            for (OrderCommodity orderCommodity : orderCommodities) {
                OrderCommodityDto orderCommodityDto = new OrderCommodityDto();
                BeanUtils.copyProperties(orderCommodity, orderCommodityDto);
                Integer commodityId = orderCommodity.getCommodityId();
                Commodity commodity = commodityService.getById(commodityId);
                orderCommodityDto.setCommodityName(commodity.getCommodityName());
                orderCommodityDtos.add(orderCommodityDto);
            }
            orderDto.setOrderCommodityList(orderCommodityDtos);
            return orderDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(orderDtos);
        System.out.println(orderDtoPage);
        //当删除到页数发生变化的时候
        if (current > orderDtoPage.getPages()){
            orderDtoPage.setCurrent(orderDtoPage.getPages());
        }
        return new Result(Code.SELECT_SUCCESS,orderDtoPage);
    }


    @DeleteMapping("/{id}")
    public Result removeOrder(@PathVariable Integer id){
        LambdaQueryWrapper<OrderCommodity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderCommodity::getOrderId,id);
        boolean flag = orderCommodityService.remove(queryWrapper);
        if (flag){
            boolean flag1 = orderService.removeById(id);
            if (flag1){
                return new Result(null,Code.DELETE_SUCCESS,"删除订单信息成功");
            }
        }
        return new Result(null,Code.DELETE_FAIL,"删除订单信息失败");
    }

    @GetMapping("/{orderId}")
    public Result getOrderCommodities(@PathVariable Integer orderId){
        LambdaQueryWrapper<OrderCommodity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderCommodity::getOrderId,orderId);
        List<OrderCommodity> list = orderCommodityService.list(queryWrapper);
        List<OrderCommodityDto> collect = list.stream().map((item) -> {
            OrderCommodityDto orderCommodityDto = new OrderCommodityDto();
            BeanUtils.copyProperties(item, orderCommodityDto);
            Integer commodityId = item.getCommodityId();
            Commodity commodity = commodityService.getById(commodityId);
            orderCommodityDto.setCommodity(commodity);
            return orderCommodityDto;
        }).collect(Collectors.toList());
        return new Result(Code.SELECT_SUCCESS,collect);
    }

    @GetMapping("/aftermarket/{orderCommodityId}")
    public Result getAftermarketOrderCommodities(@PathVariable Integer orderCommodityId){
        LambdaQueryWrapper<OrderCommodity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderCommodity::getId,orderCommodityId);
        List<OrderCommodity> list = orderCommodityService.list(queryWrapper);
        List<OrderCommodityDto> collect = list.stream().map((item) -> {
            OrderCommodityDto orderCommodityDto = new OrderCommodityDto();
            BeanUtils.copyProperties(item, orderCommodityDto);
            Integer commodityId = item.getCommodityId();
            Commodity commodity = commodityService.getById(commodityId);
            orderCommodityDto.setCommodity(commodity);
            return orderCommodityDto;
        }).collect(Collectors.toList());
        return new Result(Code.SELECT_SUCCESS,collect);
    }

    @GetMapping("/searchOrder/{current}/{pageSize}/{userId}/{orderId}")
    public Result getOrder(@PathVariable Integer current,@PathVariable Integer pageSize,@PathVariable Integer userId,@PathVariable Integer orderId){
        //先查询当前用户下的订单
        Page<Order> orderPage = new Page<>(current,pageSize);
        Page<OrderDto> orderDtoPage = new Page<>(current,pageSize);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getId,orderId);
        wrapper.eq(Order::getUserId,userId);
        orderService.page(orderPage, wrapper);
        BeanUtils.copyProperties(orderPage,orderDtoPage,"records");
        List<Order> records = orderPage.getRecords();
        List<OrderDto> orderDtos = records.stream().map((item) -> {
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);
            Integer userId1 = item.getUserId();
            Users users = usersService.searchUser(userId1);
            orderDto.setUsername(users.getName());
            Integer addressId = item.getAddressId();
            Address address = addressService.searchAddress(addressId);
            orderDto.setAddress(address);
            Integer id = item.getId();
            LambdaQueryWrapper<OrderCommodity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(OrderCommodity::getOrderId, id);
            List<OrderCommodity> orderCommodities = orderCommodityService.list(queryWrapper);
            List<OrderCommodityDto> orderCommodityDtos = new ArrayList<>();
            for (OrderCommodity orderCommodity : orderCommodities) {
                OrderCommodityDto orderCommodityDto = new OrderCommodityDto();
                BeanUtils.copyProperties(orderCommodity, orderCommodityDto);
                Integer commodityId = orderCommodity.getCommodityId();
                Commodity commodity = commodityService.getById(commodityId);
                orderCommodityDto.setCommodityName(commodity.getCommodityName());
                orderCommodityDtos.add(orderCommodityDto);
            }
            orderDto.setOrderCommodityList(orderCommodityDtos);
            return orderDto;
        }).collect(Collectors.toList());
        orderDtoPage.setRecords(orderDtos);
        System.out.println(orderDtoPage);
        //当删除到页数发生变化的时候
        if (current > orderDtoPage.getPages()){
            orderDtoPage.setCurrent(orderDtoPage.getPages());
        }
        return new Result(Code.SELECT_SUCCESS,orderDtoPage);
    }

}
