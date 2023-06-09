package com.jiahe.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Users;
import com.jiahe.service.UsersService;
import com.jiahe.utils.Code;
import com.jiahe.utils.RSAUtil;
import com.jiahe.utils.Result;
import com.jiahe.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/usersManagement")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/login")
    public Result login(@RequestBody Users users, HttpServletRequest request) throws Exception {
        String usernameRSA = users.getUsername();
        String passwordRSA = users.getPassword();
        users.setUsername(RSAUtil.decryptWithPrivate(usernameRSA));
        users.setPassword(RSAUtil.decryptWithPrivate(passwordRSA));
        Users checkUser = usersService.checkUsers(users);
        if (checkUser != null) {
            checkUser.setPassword(passwordRSA);
            request.getSession().setAttribute("user", checkUser);
            return new Result(checkUser, Code.LOGIN_SUCCESS, "登录成功");
        }
        else
            return new Result(0, Code.LOGIN_FAIL, "登录失败，请检查用户名/手机号和密码");
    }

    @GetMapping("/getLoginUser")
    public Result getLoginUser(HttpServletRequest request) {
        return new Result(Code.SELECT_SUCCESS, request.getSession().getAttribute("user"));
    }

    @GetMapping("/logout")
    public Result logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return new Result(0, Code.LOGOUT_SUCCESS, "已成功退出登录");
    }

    @GetMapping("/{current}/{size}")
    public Result selectByPage(@PathVariable Integer current, @PathVariable Integer size) {
        IPage<Users> page = usersService.selectByPage(current, size);
        return new Result(Code.SELECT_SUCCESS, page);
    }

    @GetMapping("/getValidateCode")
    public Result getValidateCode() {
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        System.out.println("验证码为：" + code);
        return new Result(code, Code.SELECT_SUCCESS, "验证码发送成功");
    }

    @GetMapping("/checkValidateCode")
    public Result checkValidateCode() {
        String code = ValidateCodeUtils.generateValidateCode(6).toString();
        System.out.println("验证码为：" + code);
        return new Result(code, Code.SELECT_SUCCESS, "验证码发送成功");
    }

    @PostMapping("/checkUsername")
    public Result checkUsername(@RequestBody Users users) {
        if (usersService.checkUsername(users.getUsername()))
            return new Result(1, Code.SELECT_SUCCESS, "此用户名已被注册");
        else
            return new Result(Code.SELECT_FAIL, 0);
    }

    @PostMapping("/register")
    public Result register(@RequestBody Users users) {
        if (usersService.addUsers(users))
            return new Result(1, Code.ADD_SUCCESS, "注册成功");
        else
            return new Result(0, Code.ADD_FAIL, "注册失败，已有此用户或此手机号已被注册");
    }

    @PostMapping("/deleteBatchUsers")
    public Result deleteBatchUsers(@RequestBody List<Users> users) {
        if (usersService.deleteBatchUsers(users))
            return new Result(1, Code.DELETE_SUCCESS, "批量删除成功");
        else
            return new Result(0, Code.DELETE_FAIL, "批量删除失败");
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteUsers(@PathVariable Integer id) {
        if (usersService.deleteUsers(id))
            return new Result(1, Code.DELETE_SUCCESS, "删除成功");
        else
            return new Result(0, Code.DELETE_FAIL, "删除失败，不存在此用户");
    }

    @DeleteMapping("/deleteBySelf/{id}")
    public Result deleteUserBySelf(@PathVariable Integer id, HttpServletRequest request) {
        if (usersService.deleteUsers(id)) {
            request.getSession().removeAttribute("user");
            return new Result(1, Code.DELETE_SUCCESS, "删除成功");
        }
        else
            return new Result(0, Code.DELETE_FAIL, "删除失败");
    }

    @PutMapping("/updateUsers")
    public Result updateUsers(@RequestBody Users users) {
        if (usersService.updateUsers(users))
            return new Result(1, Code.UPDATE_SUCCESS, "修改成功");
        else
            return new Result(0, Code.UPDATE_FAIL, "修改失败");
    }

    @PostMapping("/checkPhone")
    public Result checkPhone(@RequestBody Users users) {
        if (usersService.checkPhone(users.getPhone()))
            return new Result(Code.SELECT_SUCCESS, 1);
        else
            return new Result(Code.SELECT_FAIL, 1);
    }

    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestBody Users users) {
        if (usersService.updatePassword(users))
            return new Result(1, Code.UPDATE_SUCCESS, "已重置密码");
        else
            return new Result(0, Code.UPDATE_FAIL, "重置密码失败");
    }

    @PutMapping("/updateUsersInfo")
    public Result updateUsersInfo(@RequestBody Users users) {
        if (usersService.updateUsers(users))
            return new Result(1, Code.UPDATE_SUCCESS, "修改个人信息成功");
        else
            return new Result(0, Code.UPDATE_FAIL, "修改个人信息失败");
    }

    @PutMapping("/updateUsersPhone")
    public Result updateUsersPhone(@RequestBody Users users) {
        if (usersService.updateUsersPhone(users))
            return new Result(1, Code.UPDATE_SUCCESS, "修改手机号成功");
        else
            return new Result(0, Code.UPDATE_FAIL, "修改手机号失败");
    }

    @PostMapping("/search")
    public Result searchUsers(@RequestBody Users user) {
        List<Users> users = usersService.searchUsers(user);
        return new Result(users, Code.SELECT_SUCCESS, "查询成功");
    }

    @GetMapping("/search/{id}")
    public Result searchUser(@PathVariable Integer id) {
        return new Result(usersService.searchUser(id), Code.SELECT_SUCCESS,"查询成功");
    }
}
