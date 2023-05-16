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
import java.util.ArrayList;
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

    /**
     * 分页查询所有上架了的商品数据
     * @param current 当前页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/grounding/{current}/{pageSize}")
    public Result selectAllGrounding(@PathVariable int current, @PathVariable int pageSize){
        Page<Commodity> commodityPage = new Page<>(current,pageSize);
        Page<CommodityDto> commodityDtoPage = new Page<>(current,pageSize);
        LambdaQueryWrapper<Commodity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Commodity::getStatus,0);

        commodityService.page(commodityPage,queryWrapper);

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

    /**
     * 分页查询所有上架了的商品数据
     * @param current 当前页码
     * @param pageSize 每页条数
     * @return
     */
    @GetMapping("/discount/{current}/{pageSize}")
    public Result selectAllDiscount(@PathVariable int current, @PathVariable int pageSize){
        Page<Commodity> commodityPage = new Page<>(current,pageSize);
        Page<CommodityDto> commodityDtoPage = new Page<>(current,pageSize);
        LambdaQueryWrapper<Commodity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Commodity::getStatus,0);
        queryWrapper.lt(Commodity::getDiscount,1);

        commodityService.page(commodityPage,queryWrapper);

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
        Boolean addFlag = commodityService.checkAdd(commodity);
        if (addFlag == true){
            return new Result(null,Code.ADD_FAIL,"商品表中已经有同品牌的商品");
        }
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
        Boolean addFlag = commodityService.checkAdd(commodity);
        if (addFlag == true){
            return new Result(null,Code.ADD_FAIL,"商品表中已经有同品牌的商品");
        }
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

        List<CommodityDto> commodityDtos = commodities.stream().map((item) -> {
            CommodityDto commodityDto = new CommodityDto();
            BeanUtils.copyProperties(item, commodityDto);
            Category category = categoryService.getById(item.getCategoryId());
            commodityDto.setCategoryName(category.getCategoryName());
            return commodityDto;
        }).collect(Collectors.toList());

        return new Result(commodityDtos,Code.SELECT_SUCCESS,"共查询到"+commodityDtos.size()+"条数据");
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

    @GetMapping("/rotated")
    public Result getRotated(){
        LambdaQueryWrapper<Commodity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Commodity::getStatus,2);
        List<Commodity> commodities = commodityService.list(queryWrapper);
        return new Result(Code.SELECT_SUCCESS,commodities);
    }
}
