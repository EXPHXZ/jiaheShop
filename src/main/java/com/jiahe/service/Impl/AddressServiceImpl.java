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

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;
    //检查默认是否有冲突
    public List<Address> checkDefault(){
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getIsDefault,1);
        return addressDao.selectList(lqw);
    }
    //分页查询
    @Override
    public IPage<Address> selectByPage(Integer id,Integer current, Integer size) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getUserId,id);
        IPage<Address> page = new Page<Address>(current, size);
        System.out.println(page);
        return addressDao.selectPage(page,lqw);
    }

    @Override
    public Boolean addPersonalAddress(Address address) {
        if (checkDefault() != null)
            return false;
        else
            return addressDao.insert(address) > 0;
    }

    @Override
    public Address searchUpdateAddress(String location) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getLocation,location);
        return addressDao.selectOne(lqw);
    }

}
