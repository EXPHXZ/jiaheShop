package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Admins;
import com.jiahe.pojo.Users;

import java.util.List;

public interface UsersService {

    public Users checkUsers(Users users);

    public Boolean checkUsername(String username);

    public Boolean checkPhone(String phone);

    public IPage<Users> selectByPage(Integer current, Integer size);

    public Boolean addUsers(Users users);

    public Boolean deleteUsers(Integer id);

    public Boolean deleteBatchUsers(List<Users> users);

    public Boolean updateUsers(Users users);

    public Boolean updatePassword(Users users);

    public Boolean updateUsersPhone(Users users);

    public List<Users> searchUsers(Users users);

    public Users searchUser(Integer id);

}
