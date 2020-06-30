package com.lening.service;

import com.lening.entity.CityBean;
import com.lening.entity.UserBean;
import com.lening.utils.PageResult;

import java.util.List;

public interface UserService {

    boolean register(UserBean userBean);

    PageResult findPageHelper(Integer pageNum, Integer pageSize);

    PageResult findPage(Integer pageNum, Integer pageSize);

    List<CityBean> findCityByPid(Integer pid);

    UserBean findOne(Long uid);

    void save(UserBean userBean);

    void deletes(Long[] ids);

    List<UserBean> findAll();

    PageResult findPageHelperAndCon(UserBean usermohu, Integer pageNum, Integer pageSize);

    UserBean userActivation(String ucode);

    void upaUser(UserBean userBean);

    UserBean findUserByName(String uname);

    UserBean findUserByTel(Long tel);
}
