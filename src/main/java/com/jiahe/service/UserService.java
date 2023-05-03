package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    public User checkUser(User user);

//  分页查询
    public IPage<User> selectByPage(Integer current, Integer size);

    public Boolean addUser(User user);

//  根据用户id单条删除数据
    public Boolean deleteUser(Integer id);

//  批量删除数据
    public Boolean deleteUsers(List<User> users);

    public Boolean updateUser(User user);

    public List<User> searchUser(User user);

//  根据用户id查找要修改的用户信息并回显到修改表上
    public User searchUpdateUser(Integer id);
}
