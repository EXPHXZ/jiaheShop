package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiahe.dao.UserDao;
import com.jiahe.pojo.User;
import com.jiahe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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

//  根据用户id单条删除数据
    @Override
    public Boolean deleteUser(Integer id) {
        if (userDao.selectById(id) == null)
            return false;
        else
            return userDao.deleteById(id) > 0;
    }

//  批量删除数据
    @Override
    public Boolean deleteUsers(List<User> users){
        List<Integer> idList = new ArrayList<Integer>();
        for (User user : users)
            idList.add(user.getId());
        return userDao.deleteBatchIds(idList) > 0;
    }

    @Override
    public Boolean updateUser(User user) {
        if (userDao.selectById(user.getId()) == null)
            return false;
        else
            return userDao.updateById(user) > 0;
    }
}
