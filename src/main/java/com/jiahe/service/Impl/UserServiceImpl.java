package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dao.UserDao;
import com.jiahe.dto.OrderDto;
import com.jiahe.pojo.Commodity;
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
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getPassword, user.getPassword());
        User user1 = userDao.selectOne(wrapper);
        return user1;
    }

//  分页查询
    @Override
    public IPage<User> selectAll(Integer current,Integer size){
        IPage<User> page = new Page<User>(current,size);
        return userDao.selectPage(page,null);
    }

//  新增用户
    @Override
    public Boolean addUser(User user) {
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

//    修改用户信息
    @Override
    public Boolean updateUser(User user) {
        if (userDao.selectById(user.getId()) == null)
            return false;
        else
            return userDao.updateById(user) > 0;
    }
}
