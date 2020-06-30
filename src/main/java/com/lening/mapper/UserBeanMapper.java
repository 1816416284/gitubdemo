package com.lening.mapper;

import com.lening.entity.AddressBean;
import com.lening.entity.CityBean;
import com.lening.entity.UserBean;
import com.lening.entity.Usermohu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserBeanMapper {

    void insertUser(UserBean userBean);
    List<UserBean> findPage(@Param("pageNum") Integer pageNum,@Param("pageSize")Integer pageSize);

    int deleteByPrimaryKey(Long uid);

    int insertSelective(UserBean record);

    UserBean selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(UserBean record);

    List<UserBean> findAll();

    List<AddressBean> findAddressByUid(@Param("id") Long id);

    List<CityBean> findCityByPid(@Param("pid") Integer pid);

    void deleteAddressByUid(@Param("id") Long id);

    void insertAddressUpa(AddressBean addressBean);

    void insertAddress(AddressBean addressBean);

    List<UserBean> findPageCon(Usermohu usermohu);

    UserBean selectUserByCode(@Param("code") String code);

    void upaUser(UserBean userBean);

    UserBean findUserByName(@Param("uname") String uname);

    UserBean findUserByTel(@Param("tel") Long tel);
}