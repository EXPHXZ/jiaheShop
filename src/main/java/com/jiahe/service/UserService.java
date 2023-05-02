package com.jiahe.service;

import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    public User checkUser(User user);


    public Boolean addUser(User user);

    public Boolean deleteUser(User user);

    public Boolean updateUser(User user);
}
