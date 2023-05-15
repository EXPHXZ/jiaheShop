package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiahe.dao.AdminsDao;
import com.jiahe.pojo.Admins;
import com.jiahe.service.AdminsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminsServiceImpl implements AdminsService {

    @Autowired
    private AdminsDao adminsDao;

    @Override
    public Admins checkAdmins(Admins admins) {
        LambdaQueryWrapper<Admins> lqw = new LambdaQueryWrapper<Admins>();
        lqw.eq(Admins::getAccount, admins.getAccount());
        lqw.eq(Admins::getPassword, admins.getPassword());
        return adminsDao.selectOne(lqw);
    }

    @Override
    public Boolean checkAccount(String account) {
        LambdaQueryWrapper<Admins> lqw = new LambdaQueryWrapper<Admins>();
        lqw.eq(Admins::getAccount, account);
        if (adminsDao.selectOne(lqw) != null)
            return true;
        return false;
    }

    @Override
    public IPage<Admins> selectByPage(Integer current, Integer size) {
        IPage<Admins> page = new Page<Admins>(current, size);
        return adminsDao.selectPage(page, null);
    }

    @Override
    public Boolean addAdmins(Admins admins) {
        if (checkAccount(admins.getAccount()))
            return false;
        return adminsDao.insert(admins) > 0;
    }

    @Override
    public Boolean deleteAdmins(Integer id) {
        return adminsDao.deleteById(id) > 0;
    }

    @Override
    public Boolean deleteBatchAdmins(List<Admins> admins) {
        List<Integer> idList = new ArrayList<Integer>();
        for (Admins admin : admins)
            idList.add(admin.getId());
        return adminsDao.deleteBatchIds(idList) > 0;
    }

    @Override
    public Boolean updateAdmins(Admins admins) {
        if (checkAccount(admins.getAccount()))
            return false;
        return adminsDao.updateById(admins) > 0;
    }

    @Override
    public Boolean updateAdminsInfo(Admins admins) {
        return adminsDao.updateById(admins) > 0;
    }

    @Override
    public List<Admins> searchAdmins(Admins admins) {
        LambdaQueryWrapper<Admins> lqw = new LambdaQueryWrapper<Admins>();
        lqw.eq(admins.getId() != null, Admins::getId, admins.getId());
        lqw.like(admins.getAccount() != null, Admins::getAccount, admins.getAccount());
        lqw.like(admins.getIdentity() != null, Admins::getIdentity, admins.getIdentity());
        return adminsDao.selectList(lqw);
    }

    @Override
    public Admins searchAdmin(Integer id) {
        return adminsDao.selectById(id);
    }
}
