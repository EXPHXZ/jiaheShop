package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dao.UserDao;
import com.jiahe.dto.OrderDto;
import com.jiahe.pojo.Commodity;
import com.jiahe.pojo.User;
import com.jiahe.service.UserService;
import com.jiahe.utils.Code;
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
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>();
        wrapper.eq(User::getUsername, user.getUsername());
        wrapper.eq(User::getPassword, user.getPassword());
        return userDao.selectOne(wrapper);
    }

//  分页查询
    @Override
    public IPage<User> selectByPage(Integer current,Integer size){
        IPage<User> page = new Page<User>(current,size);
        return userDao.selectPage(page,null);
    }

//  新增用户
    @Override
    public Boolean addUser(User user) {
        if(checkUser(user) != null)
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

//    修改用户信息
    @Override
    public Boolean updateUser(User user) {
        if (checkUser(user) != null)
            return false;
        else
            return userDao.updateById(user) > 0;
    }

//  查询用户
    @Override
    public List<User> searchUser(User user){
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<User>();
        lqw.eq(user.getId() != null,User::getId,user.getId());
        lqw.like(user.getUsername() != null,User::getUsername,user.getUsername());
        lqw.like(user.getIdentity() != null,User::getIdentity, user.getIdentity());
        return userDao.selectList(lqw);
    }

//  根据用户id查找要修改的用户信息并回显到修改表上
    @Override
    public User searchUpdateUser(Integer id){
        return userDao.selectById(id);
    }
}
