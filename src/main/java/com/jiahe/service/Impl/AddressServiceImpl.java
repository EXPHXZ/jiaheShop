package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dao.AddressDao;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Users;
import com.jiahe.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    //分页查询
    @Override
    public IPage<Address> selectByPage(Users user,Integer current, Integer size) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getUserId,user.getId());
        IPage<Address> page = new Page<Address>(current, size);
        return addressDao.selectPage(page,lqw);
    }

    @Override
    public Boolean addPersonalAddress(Address address,Users user) {
        address.setUserId(user.getId());
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getIsDefault,0);
        addressDao.selectList(lqw);
        return addressDao.insert(address) > 0;
    }

}
