package com.jiahe.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.pojo.Aftermarket;
import com.jiahe.pojo.Order;
import com.jiahe.service.AftermarketService;
import com.jiahe.service.OrderService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author  
 * @since 2023-04-29
 */
@RestController
@RequestMapping("/aftermarket")
public class AftermarketController {

    @Autowired
    AftermarketService aftermarketService;

    @Autowired
    OrderService orderService;
/*
* 这段代码定义了一个名为 AftermarketController 的 RestController 类，
* 其包含一个名为 "aftermarket" 的请求处理函数。

请求处理函数的标识符为 "/aftermarket",使用 @RequestMapping 注解标记。

@RestController 注解表示该控制器类是一个 Restful 控制器，它将处理所有来自 Web 客户端的 RESTful API 请求。

@RequestMapping("/aftermarket") 注解指定了请求的 URL 路径为 "/aftermarket",
* 该注解会将该请求映射到 AftermarketController 类中的某个请求处理函数上。

在 AftermarketController 类中，
* 我们使用了 @Autowired 注解来自动注入一个 aftermarketService 实例和一个 orderService 实例。
* 这两个实例都是通过 Spring 的依赖注入机制来注入的。

因为 aftermarketService 和 orderService 实例都需要依赖 AftermarketController 类，
* 所以 Spring 会自动查找 AftermarketController 类中声明的 @Autowired 注解，并根据需要自动注入依赖。
* */
    /**
     * 获取所有的售后信息
     * @return
     */

    @GetMapping("/getAll")
    public Result getAll(){
        //获取没有删除的订单列表
        List<Aftermarket> list = aftermarketService.list(
                new LambdaQueryWrapper<Aftermarket>().eq(Aftermarket::getIsDeleted,0));
        //遍历售后信息根据订单id获取到的对应的订单状态
        for(Aftermarket aftermarket:list){
            Integer orderId = aftermarket.getOrderId();
            //调用orderService写好的查询订单功能查询到对于的订单信息，拿到订单状态
            Order order = orderService.searchOrder(orderId);
            aftermarket.setStatus(order.getStatus());
        }

        return new Result(Code.SELECT_SUCCESS,list);
    }
/*
这段代码定义了一个名为 getAll 的请求处理函数，该函数接受一个 GET 请求并返回一个 Result 对象。

@GetMapping("/getAll") 注解指定了请求的 URL 路径为 "/getAll",该注解会将该请求映射到 getAll 函数上。

public Result getAll() 函数返回一个 Result 对象，Result 对象表示一个请求的结果，
通常表示一个 HTTP 响应。在这个函数中，我们使用 LambdaQueryWrapper 类来查询没有删除的订单列表，
并使用 for 循环遍历列表中的每个售后信息，根据订单 ID 获取对应的订单状态，并将状态更新到售后信息中。

最后，我们返回一个 Result 对象，其中 Code 属性为 SELECT_SUCCESS，表示请求成功。
list 属性为查询得到的订单列表。
 */


    /*@PostMapping("/add")
    public Result add(@RequestBody Aftermarket aftermarket){
        aftermarket.setIsDeleted(0);
        boolean save = aftermarketService.save(aftermarket);
        if (save){
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        }
        return new Result(null,Code.ADD_FAIL,"添加失败");
    }*/

    /**
     * 修改售后信息
     * @param aftermarket
     * @return
     */
    /*这段代码定义了一个名为 update 的请求处理函数，该函数接受一个 PUT 请求并返回一个 Result 对象。

@PutMapping("/update") 注解指定了请求的 URL 路径为 "/update",该注解会将该请求映射到 update 函数上。

public Result update(@RequestBody Aftermarket aftermarket)
函数接受一个 @RequestBody 类型的参数 aftermarket，该参数表示需要更新的售后信息。

在函数中，我们使用 aftermarketService.updateById(aftermarket) 方法来更新售后信息。
如果更新成功，我们返回一个 Result 对象，其中 Code 属性为 UPDATE_SUCCESS，表示请求成功，
同时返回一个字符串，表示修改成功的内容。如果更新失败，
我们返回一个 Result 对象，其中 Code 属性为 UPDATE_FAIL，表示请求失败，
同时返回一个字符串，表示修改失败的内容。

总之，这个函数用于更新售后信息，它接收一个 @RequestBody 类型的参数 aftermarket，
并使用 aftermarketService.updateById(aftermarket) 方法来更新售后信息。如果更新成功，
返回一个 Result 对象，其中 Code 属性为 UPDATE_SUCCESS，表示请求成功，
同时返回一个字符串，表示修改成功的内容;如果更新失败，返回一个 Result 对象，
其中 Code 属性为 UPDATE_FAIL，表示请求失败，同时返回一个字符串，表示修改失败的内容。
    *
    * */
    @PostMapping("/add")
    public Result add(@RequestBody Aftermarket aftermarket){
        Boolean flag = aftermarketService.addAftermarket(aftermarket);
        if(flag)
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        else
            return new Result(null,Code.ADD_FAIL,"添加失败");
    }





    @PostMapping("/delete")
    public Result handleReturnCommodity(@RequestBody Aftermarket aftermarket){
        Integer id = aftermarket.getId();
        Integer orderId = aftermarket.getOrderId();
        //处理退货时，先修改订单项的订单状态，然后再修改订单的状态
        Order order = orderService.searchOrder(orderId);
        //把订单下面需要退货的订单的状态改成2已退货
        Boolean flag2 = orderService.updateOrderDetailForAftermarket(orderId);
        //把订单状态改成0-正常订单
        Boolean flag1 = orderService.updateOrderForAftermarket(order);
        //删除订单
        Boolean flag = aftermarketService.removeById(id);
        return new Result(null,flag&&flag1&&flag2? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"处理退货成功":"处理退货失败");
    }

}
/*这段代码定义了一个名为 handleReturnCommodity 的请求处理函数，该函数接受一个 POST 请求并返回一个 Result 对象。

@PostMapping("/delete") 注解指定了请求的 URL 路径为 "/delete",
该注解会将该请求映射到 handleReturnCommodity 函数上。

public Result handleReturnCommodity(@RequestBody Aftermarket aftermarket)
函数接受一个 @RequestBody 类型的参数 aftermarket，该参数表示需要处理的退货信息。

在函数中，我们首先获取退货信息 ID，然后获取订单 ID，使用 orderService.searchOrder(orderId) 方法查询订单，
并更新订单详情和订单状态。接着使用 aftermarketService.removeById(id) 方法删除退货信息。
最后，我们返回一个 Result 对象，其中 Code 属性为 DELETE_SUCCESS 或 DELETE_FAIL，
表示请求成功或失败，同时返回处理退货成功的字符串提示信息。

总之，这个函数用于处理退货，它接收一个 @RequestBody 类型的参数 aftermarket，
并使用 orderService.searchOrder(orderId) 方法查询订单，并更新订单详情和订单状态。
最后，使用 aftermarketService.removeById(id) 方法删除退货信息，并返回一个 Result 对象，
其中 Code 属性为 DELETE_SUCCESS 或 DELETE_FAIL，表示请求成功或失败，同时返回处理退货成功的字符串提示信息*/

