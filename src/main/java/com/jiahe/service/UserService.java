package com.jiahe.service;

import com.jiahe.pojo.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    public User login(User user);
}
