package com.jiahe.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dto.CommodityDto;
import com.jiahe.pojo.Category;
import com.jiahe.pojo.Commodity;
import com.jiahe.service.CategoryService;
import com.jiahe.service.CommodityService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/commodity")
public class CommodityController {

    @Autowired
    private CommodityService commodityService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询所有商品数据
     * @param current 当前页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/{current}/{pageSize}")
    public Result selectAll(@PathVariable int current, @PathVariable int pageSize){
        Page<Commodity> commodityPage = new Page<>(current,pageSize);
        Page<CommodityDto> commodityDtoPage = new Page<>(current,pageSize);

        commodityService.page(commodityPage);

        BeanUtils.copyProperties(commodityPage,commodityDtoPage,"records");

        List<Commodity> records = commodityPage.getRecords();

        List<CommodityDto> list = records.stream().map((item) -> {
            CommodityDto commodityDto = new CommodityDto();
            BeanUtils.copyProperties(item, commodityDto);
            int categoryId = commodityDto.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                commodityDto.setCategoryName(category.getCategoryName());
            }
            return commodityDto;
        }).collect(Collectors.toList());

        commodityDtoPage.setRecords(list);

        //当删除到页数发生变化的时候
        if (current > commodityDtoPage.getPages()){
            commodityDtoPage.setCurrent(commodityDtoPage.getPages());
        }

        return new Result(Code.SELECT_SUCCESS,commodityDtoPage);



    }

    @PostMapping
    public Result addCommodity(@RequestBody Commodity commodity) throws Exception {
        System.out.println("商品添加------------------");
        System.out.println(commodity);
        Boolean flag = commodityService.addCommodity(commodity);
        if(flag){
            return new Result(null,Code.ADD_SUCCESS,"添加成功");
        }else {
            return new Result(null,Code.ADD_FAIL,"添加失败");
        }

    }

    /**
     * 根据id删除商品数据
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteCommodity(@PathVariable Integer id){
        Boolean flag = commodityService.deleteCommodity(id);
        return new Result(null,flag? Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"删除成功":"删除失败");
    }

    /**
     * 根据id查询商品数据来回显数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result searchCommodity(@PathVariable Integer id){
        CommodityDto commodityDto = new CommodityDto();
        Commodity commodity = commodityService.searchCommodity(id);
        BeanUtils.copyProperties(commodity,commodityDto);
        Category categoryServiceById = categoryService.getById(commodity.getCategoryId());
        String categoryName = categoryServiceById.getCategoryName();
        commodityDto.setCategoryName(categoryName);
        return new Result(Code.SELECT_SUCCESS,commodityDto);
    }

    @PutMapping
    public Result updateCommodity(@RequestBody Commodity commodity){
        Boolean flag = commodityService.updateCommodity(commodity);
        return new Result(null,flag?Code.UPDATE_SUCCESS:Code.UPDATE_FAIL,flag?"修改成功":"修改失败");
    }

    /**
     * 根据传入的json实体类来进行条件查询
     * @param commodity
     * @return
     */
    @PostMapping("/search")
    public Result search(@RequestBody Commodity commodity){
        List<Commodity> commodities = commodityService.searchCommodity(commodity);
        return new Result(commodities,Code.SELECT_SUCCESS,"共查询到"+commodities.size()+"条数据");
    }

    /**
     * 根据传来的Json数据列表来进行批量删除
     * @param commodities
     * @return
     */
    @PostMapping("/delete")
    public Result deleteCommodities(@RequestBody List<Commodity> commodities){
        Boolean flag = commodityService.deleteCommodities(commodities);
        return new Result(null,flag?Code.DELETE_SUCCESS:Code.DELETE_FAIL,flag?"批量删除成功":"批量删除失败");
    }

    @GetMapping("/category")
    public Result getCategory(){
        List<Category> categories = categoryService.list();
        return new Result(Code.SELECT_SUCCESS,categories);
    }
}
