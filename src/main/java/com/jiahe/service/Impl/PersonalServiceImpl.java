package com.jiahe.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiahe.dao.PersonalDao;
import com.jiahe.pojo.Personal;
import com.jiahe.service.PersonalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Autowired
    private PersonalDao personalDao;

    @Override
    public Boolean addPersonalInfo(Personal personal) {
        return personalDao.insert(personal) > 0;
    }

    @Override
    public Boolean deletePersonalInfoByUid(Integer uid) {
        LambdaQueryWrapper<Personal> lqw = new LambdaQueryWrapper<Personal>();
        lqw.eq(Personal::getUserId, uid);
        return personalDao.delete(lqw) > 0;
    }

    @Override
    public Personal searchPersonalInfoByUid(Integer uid) {
        LambdaQueryWrapper<Personal> lqw = new LambdaQueryWrapper<Personal>();
        lqw.eq(Personal::getUserId, uid);
        return personalDao.selectOne(lqw);
    }

    @Override
    public Boolean updatePersonalInfo(Personal personal) {
        return personalDao.updateById(personal) > 0;
    }

}
