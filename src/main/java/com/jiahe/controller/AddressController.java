package com.jiahe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Admins;
import com.jiahe.pojo.Users;
import com.jiahe.service.AddressService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usersAddress")
public class AddressController {

    @Autowired
    private AddressService addressService;

//    分页查询
    @GetMapping("/{current}/{size}")
    public Result selectByPage(@RequestBody Users user, @PathVariable Integer current, @PathVariable Integer size) {
        IPage<Address> page = addressService.selectByPage(user, current, size);
        return new Result(Code.SELECT_SUCCESS, page);
    }

//    用户增加个人地址
    @PostMapping("/addPersonalAddress")
    public Result addPersonalAddress(@RequestBody Address address,@RequestBody Users user){
        Boolean flag = addressService.addPersonalAddress(address,user);
        return null;
    }

}
