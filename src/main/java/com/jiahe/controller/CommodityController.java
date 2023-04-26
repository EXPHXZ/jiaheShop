package com.jiahe.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Commodity;
import com.jiahe.service.CommodityService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @GetMapping("/{current}/{pageSize}")
    public Result selectAll(@PathVariable int current, @PathVariable int pageSize){
        IPage<Commodity> page = commodityService.SelectAll(current,pageSize);
        System.out.println(page);

        //当删除到页数发生变化的时候
        if (current > page.getPages()){
            page = commodityService.SelectAll((int)page.getPages(),pageSize);
        }
        return new Result(Code.SELECT_SUCCESS,page);
    }

    @PostMapping
    public Result addCommodity(@RequestBody Commodity commodity){
        System.out.println(commodity);
        Boolean flag = commodityService.addCommodity(commodity);
        return new Result(null,flag == true? Code.ADD_SUCCESS:Code.ADD_FAIL,"添加成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteCommodity(@PathVariable Integer id){
        System.out.println(id);
        Boolean flag = commodityService.deleteCommodity(id);
        return new Result(null,flag == true? Code.DELETE_SUCCESS:Code.DELETE_FAIL,"删除成功");
    }

    @GetMapping("/{id}")
    public Result searchCommodity(@PathVariable Integer id){
        Commodity commodity = commodityService.searchCommodity(id);
        return new Result(Code.SELECT_SUCCESS,commodity);
    }

    @PutMapping
    public Result updateCommodity(@RequestBody Commodity commodity){
        Boolean flag = commodityService.updateCommodity(commodity);
        return new Result(null,Code.UPDATE_SUCCESS,"修改成功");
    }

    @PostMapping("/search")
    public Result search(@RequestBody Commodity commodity){
        List<Commodity> commodities = commodityService.searchCommodity(commodity);
        return new Result(commodities,Code.SELECT_SUCCESS,"共查询到"+commodities.size()+"条数据");
    }

    @PostMapping("/delete")
    public Result deleteCommodities(@RequestBody List<Commodity> commodities){
        Boolean flag = commodityService.deleteCommodities(commodities);
        return new Result(null,Code.DELETE_SUCCESS,"批量删除成功");
    }
}
