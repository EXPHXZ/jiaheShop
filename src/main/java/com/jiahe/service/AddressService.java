package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Users;
import java.util.List;

public interface AddressService {
    //分页查询
    public IPage<Address> selectByPage(Integer current, Integer size);

    public List<Address> searchPersonalAddress(Users user);
}
