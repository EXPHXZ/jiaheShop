package com.jiahe.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiahe.pojo.Admins;

import java.util.List;

public interface AdminsService {

    public Admins checkAdmins(Admins admins);

    public Boolean checkAccount(String account);

    public IPage<Admins> selectByPage(Integer current, Integer size);

    public Boolean addAdmins(Admins admins);

    public Boolean deleteAdmins(Integer id);

    public Boolean deleteBatchAdmins(List<Admins> admins);

    public Boolean updateAdmins(Admins admins);

    public Boolean updateAdminsInfo(Admins admins);

    public List<Admins> searchAdmins(Admins admins);

    public Admins searchAdmin(Integer id);

}
