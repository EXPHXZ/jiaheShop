package com.jiahe.service.Impl;

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

    //分页查询
    @Override
    public IPage<Address> selectByPage(Integer current, Integer size) {
        IPage<Address> page = new Page<Address>(current, size);
        return addressDao.selectPage(page, null);
    }

    @Override
    public List<Address> searchPersonalAddress(Users user) {
        return null;
    }


}
