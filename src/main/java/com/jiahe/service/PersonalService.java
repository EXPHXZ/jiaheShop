package com.jiahe.service;

import com.jiahe.pojo.Personal;

public interface PersonalService {

    public Boolean addPersonalInfo(Personal personal);

    public Boolean deletePersonalInfoByUid(Integer uid);

    public Personal searchPersonalInfoByUid(Integer uid);

    public Boolean updatePersonalInfo(Personal personal);

}
