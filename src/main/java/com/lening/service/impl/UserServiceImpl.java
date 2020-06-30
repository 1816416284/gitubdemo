package com.lening.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lening.entity.AddressBean;
import com.lening.entity.CityBean;
import com.lening.entity.UserBean;
import com.lening.entity.Usermohu;
import com.lening.mapper.UserBeanMapper;
import com.lening.service.UserService;
import com.lening.utils.PageResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserBeanMapper userBeanMapper;

    public boolean register(UserBean userBean) {
        try {
            userBeanMapper.insertUser(userBean);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    //pageHelper分页
    public PageResult findPageHelper(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<UserBean> list = userBeanMapper.findAll();
        Page<UserBean> page = (Page<UserBean>) list;
        return new PageResult(page.getTotal(), page.getResult());
    }

    //pageHelper加条件
    public PageResult findPageHelperAndCon(UserBean user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Usermohu usermohu = dateForMat(user);
        Page<UserBean> page = (Page<UserBean>) userBeanMapper.findPageCon(usermohu);
        return new PageResult(page.getTotal(), page.getResult());
    }

    public UserBean userActivation(String ucode) {

        return userBeanMapper.selectUserByCode(ucode);
    }

    public void upaUser(UserBean userBean) {
        userBeanMapper.upaUser(userBean);
    }

    public UserBean findUserByName(String uname) {
        return userBeanMapper.findUserByName(uname);
    }

    public UserBean findUserByTel(Long tel) {
        return userBeanMapper.findUserByTel(tel);
    }


    // sql分页
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        List<UserBean> list = userBeanMapper.findAll();
        List<UserBean> page1 = userBeanMapper.findPage((pageNum - 1) * pageSize + 1, pageNum * pageSize);
        return new PageResult(list.size(), page1);
    }

    public List<UserBean> findAll() {

        List<UserBean> list = userBeanMapper.findAll();

        return list;
    }


    public List<CityBean> findCityByPid(Integer pid) {
        return userBeanMapper.findCityByPid(pid);
    }

    public UserBean findOne(Long uid) {
        UserBean userBean = userBeanMapper.selectByPrimaryKey(uid);
        List<AddressBean> addressBeans = userBeanMapper.findAddressByUid(userBean.getId());
        userBean.setAddressOptionList(addressBeans);
        return userBean;
    }

    //修改和新增的方法
    public void save(UserBean userBean) {
        if (userBean != null) {
            if (userBean.getId() != null) {//修改
                //用户信息直接修改
                userBeanMapper.updateByPrimaryKeySelective(userBean);

                //地址的修改需要先删除再添加
                userBeanMapper.deleteAddressByUid(userBean.getId());

                if (userBean.getAddressOptionList() != null && userBean.getAddressOptionList().size() > 0) {
                    for (AddressBean addressBean : userBean.getAddressOptionList()) {
                        addressBean.setId(userBean.getId());
                        userBeanMapper.insertAddressUpa(addressBean);
                    }
                }
            } else { //新增
                //新增后的id通过sq主键表获取就可以
                userBeanMapper.insertSelective(userBean);
                //直接新增就可以
                if (userBean.getAddressOptionList() != null && userBean.getAddressOptionList().size() > 0) {
                    for (AddressBean addressBean : userBean.getAddressOptionList()) {
                        userBeanMapper.insertAddress(addressBean);
                    }
                }

            }
        }
    }

    public void deletes(Long[] ids) {
        for (Long id : ids) {
            //先删用户表再删address表
            userBeanMapper.deleteByPrimaryKey(id);
            userBeanMapper.deleteAddressByUid(id);
        }
    }

    //没有使用处理字段格式的工具包前我们自己做字段规定处理
    public Usermohu dateForMat(UserBean user) {
        Usermohu usermohu = new Usermohu();
        DateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        if (user != null) {
            if (user.getUname() != null && user.getUname().length() > 0) {
                usermohu.setUname(user.getUname());
            }
            if (user.getBrithday() != null) {
                usermohu.setBrithday(sd.format(user.getBrithday()));
            }
            if (user.getEbrithday() != null) {
                usermohu.setEbrithday(sd.format(user.getEbrithday()));
            }
        }
        return usermohu;
    }

}
