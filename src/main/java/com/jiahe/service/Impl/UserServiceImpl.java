package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.dao.UserDao;
import com.jiahe.pojo.User;
import com.jiahe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserName, user.getUserName());
        wrapper.eq(User::getPassword, user.getPassword());
        User user1 = userDao.selectOne(wrapper);
        return user1;
    }

    @Override
    public Boolean addUser(User user) {
        if (checkUser(user) == null)
            return false;
        else
            return userDao.insert(user) > 0;
    }

    @Override
    public Boolean deleteUser(User user) {
        if (userDao.selectById(user.getId()) == null)
            return false;
        else
            return userDao.deleteById(user.getId()) > 0;
    }

    @Override
    public Boolean updateUser(User user) {
        if (userDao.selectById(user.getId()) == null)
            return false;
        else
            return userDao.updateById(user) > 0;
    }
}
