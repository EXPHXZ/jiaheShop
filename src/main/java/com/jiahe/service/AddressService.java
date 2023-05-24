package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Address;
import com.jiahe.pojo.Users;
import java.util.List;

public interface AddressService {
//    分页查询
    public IPage<Address> selectByPage(Integer userId,Integer current, Integer size);
//    查询当前用户的全部地址
    public List<Address> searchPersonalAll(Integer id);
//    添加地址
    public Boolean addPersonalAddress(Address address);

//    查询要修改的个人地址
    public Address searchUpdateAddress(String location);

    public Boolean updatePersonalAddress(Address address);
}
