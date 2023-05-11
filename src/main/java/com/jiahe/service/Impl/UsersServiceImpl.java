package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dao.UsersDao;
import com.jiahe.pojo.Users;
import com.jiahe.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersDao usersDao;

    @Override
    public Users checkUsers(Users users) {
        LambdaQueryWrapper<Users> lqw = new LambdaQueryWrapper<Users>();
        lqw.eq(Users::getUsername, users.getUsername());
        lqw.eq(Users::getPassword, users.getPassword());
        return usersDao.selectOne(lqw);
    }

    @Override
    public IPage<Users> selectByPage(Integer current, Integer size) {
        IPage<Users> page = new Page<Users>(current, size);
        return usersDao.selectPage(page, null);
    }

//    @Override
//    public Boolean addUsers(Users users) {
//        if (checkAdmins(users) != null)
//            return false;
//        return adminsDao.insert(users) > 0;
//    }

    @Override
    public Boolean deleteUsers(Integer id) {
        return usersDao.deleteById(id) > 0;
    }

    @Override
    public Boolean deleteBatchUsers(List<Users> users) {
        List<Integer> idList = new ArrayList<Integer>();
        for (Users user : users)
            idList.add(user.getId());
        return usersDao.deleteBatchIds(idList) > 0;
    }

    @Override
    public Boolean updateUsers(Users users) {
        return usersDao.updateById(users) > 0;
    }

    @Override
    public List<Users> searchUsers(Users users) {
        LambdaQueryWrapper<Users> lqw = new LambdaQueryWrapper<Users>();
        lqw.eq(users.getId() != null, Users::getId, users.getId());
        lqw.like(users.getUsername() != null, Users::getUsername, users.getUsername());
        return usersDao.selectList(lqw);
    }

    @Override
    public Users searchUser(Integer id) {
        return usersDao.selectById(id);
    }
}
