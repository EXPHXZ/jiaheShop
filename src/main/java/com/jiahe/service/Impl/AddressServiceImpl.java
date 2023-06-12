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
//    分页查询
    @Override
    public IPage<Address> selectByPage(Integer id,Integer current, Integer size) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getUserId,id);
        IPage<Address> page = new Page<Address>(current, size);
        System.out.println(page);
        return addressDao.selectPage(page,lqw);
    }

//    查询当前用户的全部地址
    @Override
    public List<Address> searchPersonalAll(Integer id){
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getUserId,id);
        return addressDao.selectList(lqw);
    }
//  新增收货地址
    @Override
    public Boolean addPersonalAddress(Address address) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getIsDefault,0);
        Address address1 = addressDao.selectOne(lqw);
        if(address == null)
            System.out.println(123);
        if(address1 == null || address.getIsDefault() == 1)
            return addressDao.insert(address) > 0;
        else{
            address1.setIsDefault(1);
            addressDao.updateById(address1);
            return  addressDao.insert(address) > 0;
        }
    }
//  搜索要修改的回显地址信息
    @Override
    public Address searchUpdateAddress(Integer id) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getId,id);
        return addressDao.selectOne(lqw);
    }
//    修改地址
    @Override
    public Boolean updatePersonalAddress(Address address) {
        LambdaQueryWrapper<Address> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Address::getIsDefault,0);
        Address address1 = addressDao.selectOne(lqw);
        if (address1 != null && address.getIsDefault()==0){
            address1.setIsDefault(1);
            addressDao.updateById(address1);
        }
        return addressDao.updateById(address) > 0;
    }
//    删除地址
    @Override
    public Boolean deletePersonalAddress(Integer id) {
        return addressDao.deleteById(id) > 0;
    }

    @Override
    public Address searchAddress(Integer id) {
        LambdaQueryWrapper<Address> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Address::getId,id);
        return addressDao.selectOne(queryWrapper);
    }
}
