package com.jiahe.service;

import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.User;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {
    public User checkUser(User user);


    public Boolean addUser(User user);

//  根据用户id单条删除数据
    public Boolean deleteUser(Integer id);

//  批量删除数据
    public Boolean deleteUsers(List<User> users);

    public Boolean updateUser(User user);
}
