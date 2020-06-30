package com.lening.controller;

import com.lening.entity.CityBean;
import com.lening.entity.UserBean;
import com.lening.excel.ExcelExportUtil;
import com.lening.excel.ExcelImportUtil;
import com.lening.node.EmailSender;
import com.lening.node.NodeSendInfo;
import com.lening.service.UserService;
import com.lening.utils.PageResult;
import com.lening.utils.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Resource
    private UserService userService;
    //导入
    @RequestMapping("ExcelImport")
    public void ExcelImport(@Param("flie") MultipartFile file) throws IOException {
        List<UserBean> userBeans = new ExcelImportUtil<UserBean>(UserBean.class).readExcel(file.getInputStream(), 1, 0);
        System.out.println(userBeans);
    }
    //导出
    @RequestMapping("ExcelExport")
    public void ExcelExport(HttpServletResponse response) throws Exception {
        List<UserBean> userBeans = userService.findAll();
        System.out.println(userBeans);
        new ExcelExportUtil<UserBean>(UserBean.class,1).myExport(response,userBeans,"报表.xls");
    }
    //注册获取验证码
    @RequestMapping("getCode")
    public Result getCode(String tel, HttpServletRequest httpServletRequest) {

        try {
            //先判断session里有没有保存的验证码
            HttpSession session = httpServletRequest.getSession();
            String sessionCode = (String) session.getAttribute(tel);
            if (sessionCode != null) { //session的验证码还在的话不能再获取
                return new Result(false, "请等待验证码失效");
            } else {
                //随机生成的验证码
                String code = NodeSendInfo.GetBindNum();
                session.setAttribute(tel, code);
                System.out.println(code);
                //指定手机号发送短信
                //  String status = NodeSendInfo.NodeSend(tel, code);
                return new Result(true, code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "系统发生错误");
        }
    }
    //验证身份获取验证码
    @RequestMapping("identityCode")
    public Result identityCode(String tel, HttpServletRequest httpServletRequest) {
        try {
            //因为这个是验证所以需要先判断数据库有没有这个手机号
            UserBean userByTel = userService.findUserByTel(new Long(tel));
            if(userByTel!=null){
                //先判断session里有没有保存的验证码
                HttpSession session = httpServletRequest.getSession();
                String sessionCode = (String) session.getAttribute(tel);
                if (sessionCode != null) { //session的验证码还在的话不能再获取
                    return new Result(false, "请等待验证码失效");
                } else {
                    //随机生成的验证码
                    String code = NodeSendInfo.GetBindNum();
                    session.setAttribute(tel, code);
                    System.out.println(code);
                    //指定手机号发送短信
                    //  String status = NodeSendInfo.NodeSend(tel, code);
                    return new Result(true, code);
                }
            }
            return new Result(false,"用户绑定的手机号错误");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "系统发生错误");
        }
    }
    //验证身份
    @RequestMapping("identity")
    public Result identity(@RequestBody UserBean userBean,HttpSession session){
           UserBean userBean1 =  userService.findUserByTel(userBean.getTel());
           session.setAttribute("resetUser",userBean1.getUname());
           //获取保存的验证码
           String sessionCode = (String) session.getAttribute(String.valueOf(userBean.getTel()));
           if (sessionCode != null) {
               if (sessionCode.equals(userBean.getUcode())) {
                   session.removeAttribute(String.valueOf(userBean.getTel()));
                   return new Result(true, "验证成功");
               }else{
                   return new Result(false, "验证码错误,验证失败");
               }
           }else{
               return new Result(false, "未获取验证码或者是验证码已失效");
           }
     }

    @RequestMapping("resetPasswd")
    public Result resetPasswd(String newPassword,HttpSession session){
        String uname = (String)session.getAttribute("resetUser");
        if(uname!=null){
            try {
                UserBean userByName = userService.findUserByName(uname);
                userByName.setPassword(newPassword);
                userService.upaUser(userByName);
                return  new Result(true,"密码修改成功");
            } catch (Exception e) {
                e.printStackTrace();
                return  new Result(true,"密码修改失败");
            }
        }
        return new Result(false,"有效时间已过,需要重新验证");
    }
    //用户注册
    @RequestMapping("register")
    public Result register(@RequestBody UserBean userBean, HttpSession session) throws ParseException {
        String sessionCode = (String) session.getAttribute(String.valueOf(userBean.getTel()));
        System.out.println(sessionCode);
        if (sessionCode != null) {
            if (sessionCode.equals(userBean.getUcode())) {
                session.removeAttribute(String.valueOf(userBean.getTel()));
                //验证成功开始注册
                userBean.setStatus(0);
                userBean.setCouerror(0);
                userBean.setLocktime(new Long(0));
                userBean.setCreatetime(new Date());
                String ucode = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
                userBean.setUcode(ucode);
                boolean register = userService.register(userBean);
                if (register) {
                    //注册成功后给邮箱发送邮件进行账户激活
                    EmailSender.sendQQEmail(userBean.getEmail(), "您已注册成功!\n您的用户名是: " + userBean.getUname() + ",\n您的激活码为: " + ucode + ",\n您可以点击此链接激活账号 http://localhost:8888/activation.html ");
                    return new Result(true, "注册成功");
                } else {
                    return new Result(false, "注册失败");
                }
            } else {
                //验证码正确与否判断
                return new Result(false, "验证码错误");
            }
        } else {
            return new Result(false, "未获取验证码或者验证码已失效");
        }
    }
    //清除session验证码
    @RequestMapping("deleteCode")
    public void deleteCode(String tel, HttpSession session) {
        //时间到达的时候将验证码从session中删除
        if (session.getAttribute(tel) != null) {
            session.removeAttribute(tel);
        }
    }

    @RequestMapping("login")
    public Result login(@RequestBody UserBean userBean, HttpSession session) {

        UserBean userBean1 = userService.findUserByName(userBean.getUname());
        if (userBean1 != null) {
            if (userBean1.getStatus() == 1) {
                if (userBean1.getPassword().equals(userBean.getPassword())) {
                    userBean1.setCouerror(0); //错误数清0
                    userService.upaUser(userBean1);
                    return new Result(true, "登录成功");
                } else {
                    Integer newCou = userBean1.getCouerror() + 1;
                    if (newCou == 3) {//说明错了3次了
                        userBean1.setStatus(2); //锁住状态码锁住
                        userBean1.setCouerror(0); //错误次数清 0
                        userBean1.setLocktime(new Long(0)); //锁时间从0开始
                        userService.upaUser(userBean1);
                        return new Result(false, "不好意思你的密码连续错误三次，账户已经被锁定，20分钟后，自动解锁");
                    } else {
                        userBean1.setCouerror(newCou);
                        userService.upaUser(userBean1);
                        return new Result(false, "不好意思密码错误，您还有" + (3 - newCou) + "次机会了，要是密码连续错误三次，账户将被锁定20分钟");
                    }
                }
            }
            if (userBean1.getStatus() == 0) {
                return new Result(false, "账户未激活,请先完成激活操作");
            }
            if (userBean1.getStatus() == 2) {
                return new Result(false, "不好意思，你的账户因为密码连续错误三次，已经被锁定了，请在 "+(20-userBean1.getLocktime())+" 分钟后再来登录。");
            }
        }
        return new Result(false, "用户名错误");
    }

    @RequestMapping("acCode")
    public String acCode(String ucode) {
        UserBean userBean = userService.userActivation(ucode);
        //先查找ucode是否存在
        if (userBean != null && userBean.getId() != null) {
            //去激活用户 设置用户状态为1
            userBean.setStatus(1);
            userService.upaUser(userBean);
            return "ok!Activation the success!";
        }
        return "no!Activation failed ";
    }
   @RequestMapping("resetPassword")
   public Result resetPassword(){

        return null;
   }

    @RequestMapping("findPageAndCon")
    public PageResult findPageAndCon(@RequestBody UserBean usermohu, Integer pageNum, Integer pageSize) {
        return userService.findPageHelperAndCon(usermohu, pageNum, pageSize);
    }

    //分页查询
    @RequestMapping("findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        return userService.findPageHelper(pageNum, pageSize);
    }

    @RequestMapping("findAll")
    public List<UserBean> findAll() {
        List<UserBean> all = userService.findAll();
        System.out.println(all);
        return all;
    }

    //三级联动
    @RequestMapping("findCityByPid")
    public List<CityBean> findCityByPid(Integer pid) {

        return userService.findCityByPid(pid);
    }

    //回显
    @RequestMapping("findOne")
    public UserBean findOne(Long uid) {
        return userService.findOne(uid);
    }

    //修改和新增
    @RequestMapping("save")
    public Result save(@RequestBody UserBean userBean) {
        try {
            userService.save(userBean);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    //批量删除
    @RequestMapping("delete")
    public Result delete(@RequestBody Long[] ids) {
        try {
            userService.deletes(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
}
