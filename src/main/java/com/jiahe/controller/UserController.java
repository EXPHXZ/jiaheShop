package com.jiahe.controller;

import com.jiahe.pojo.User;
import com.jiahe.service.UserService;
import com.jiahe.utils.Code;
import com.jiahe.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User user1 = userService.login(user);
        return new Result(user1,user1 != null?Code.LOGIN_SUCCESS:Code.LOGIN_FAIL,user1 != null?"登录成功":"登录失败，请检查用户名和密码");
    }

}
