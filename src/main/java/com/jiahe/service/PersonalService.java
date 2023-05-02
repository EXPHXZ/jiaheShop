package com.jiahe.service;

import com.jiahe.pojo.Personal;

public interface PersonalService {

    public Boolean addPersonalInfo(Personal personal);

    public Boolean deletePersonalInfoById(Integer id);

    public Personal searchPersonalInfoById(Integer id);

    public Boolean updatePersonalInfo(Personal personal);

}
