package com.jiahe.service.Impl;

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
    public Boolean deletePersonalInfoById(Integer id) {
        return personalDao.deleteById(id) > 0;
    }

    @Override
    public Personal searchPersonalInfoById(Integer id) {
        return personalDao.selectById(id);
    }

    @Override
    public Boolean updatePersonalInfo(Personal personal) {
        return personalDao.updateById(personal) > 0;
    }

}
