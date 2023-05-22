package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Users;
import java.util.List;

public interface AddressService {
    //分页查询
    public IPage<Address> selectByPage(Integer userId,Integer current, Integer size);

    public Boolean addPersonalAddress(Address address);

    public Address searchUpdateAddress(String location);
}
