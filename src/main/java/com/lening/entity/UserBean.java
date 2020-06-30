package com.lening.entity;

import com.lening.excel.ExcelAttribute;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class UserBean implements Serializable {
    //ID
    @ExcelAttribute(sort = 0,name = "编号")
    private Long id;
    //用户名
    @ExcelAttribute(sort = 1,name = "名称")
    private String uname;
    //年龄
    @ExcelAttribute(sort = 2,name = "年龄")
    private Long uage;
    //出生日期
    @ExcelAttribute(sort = 3,name = "出生日期")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date brithday;
    //电话
    @ExcelAttribute(sort = 4,name = "电话")
    private  Long tel;
    //密码
    private String password;
    //邮箱地址
    private  String email;
    //状态码 0是等待激活  1是正常  2是锁定
    private  Integer status;
    //创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date createtime;
    //账户唯一激活码
    private String ucode;
    //账户锁定时间
    private Long locktime;
    //错误次数
    private Integer couerror;
    private Date ebrithday;
    private List<AddressBean> AddressOptionList = new ArrayList<AddressBean>();

    @DateTimeFormat(pattern="yyyy-MM-dd")
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}