package com.lening.node;


import com.sun.mail.util.MailSSLSocketFactory;
import org.junit.Test;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * sina.com:
 * POP3服务器地址:pop3.sina.com.cn（端口：110）
 * SMTP服务器地址:smtp.sina.com.cn（端口：25）
 *
 * sinaVIP：
 * POP3服务器:pop3.vip.sina.com （端口：110）
 * SMTP服务器:smtp.vip.sina.com （端口：25）
 *
 * sohu.com:
 * POP3服务器地址:pop3.sohu.com（端口：110）
 * SMTP服务器地址:smtp.sohu.com（端口：25）
 *
 * 126邮箱：
 * POP3服务器地址:pop.126.com（端口：110）
 * SMTP服务器地址:smtp.126.com（端口：25）
 *
 * 139邮箱：
 * POP3服务器地址：POP.139.com（端口：110）
 * SMTP服务器地址：SMTP.139.com(端口：25)
 *
 * 163.com:
 * POP3服务器地址:pop.163.com（端口：110）
 * SMTP服务器地址:smtp.163.com（端口：25）
 *
 * QQ邮箱
 * POP3服务器地址：pop.qq.com（端口：110）
 * SMTP服务器地址：smtp.qq.com （端口：25）
 *
 * QQ企业邮箱
 * POP3服务器地址：pop.exmail.qq.com （SSL启用 端口：995）
 * SMTP服务器地址：smtp.exmail.qq.com（SSL启用 端口：587/465）
 *
 * yahoo.com:
 * POP3服务器地址:pop.mail.yahoo.com
 * SMTP服务器地址:smtp.mail.yahoo.com
 *
 * yahoo.com.cn:
 * POP3服务器地址:pop.mail.yahoo.com.cn（端口：995）
 * SMTP服务器地址:smtp.mail.yahoo.com.cn（端口：587
 *
 * HotMail
 * POP3服务器地址：pop3.live.com （端口：995）
 * SMTP服务器地址：smtp.live.com （端口：587）
 *
 * gmail(google.com)
 * POP3服务器地址:pop.gmail.com（SSL启用 端口：995）
 * SMTP服务器地址:smtp.gmail.com（SSL启用 端口：587）
 *
 * 263.net:
 * POP3服务器地址:pop3.263.net（端口：110）
 * SMTP服务器地址:smtp.263.net（端口：25）
 *
 * 263.net.cn:
 * POP3服务器地址:pop.263.net.cn（端口：110）
 * SMTP服务器地址:smtp.263.net.cn（端口：25）
 *
 * x263.net:
 * POP3服务器地址:pop.x263.net（端口：110）
 * SMTP服务器地址:smtp.x263.net（端口：25）
 *
 * 21cn.com:
 * POP3服务器地址:pop.21cn.com（端口：110）
 * SMTP服务器地址:smtp.21cn.com（端口：25）
 *
 * Foxmail：
 * POP3服务器地址:POP.foxmail.com（端口：110）
 * SMTP服务器地址:SMTP.foxmail.com（端口：25）
 *
 * china.com:
 * POP3服务器地址:pop.china.com（端口：110）
 * SMTP服务器地址:smtp.china.com（端口：25）
 *
 * tom.com:
 * POP3服务器地址:pop.tom.com（端口：110）
 * SMTP服务器地址:smtp.tom.com（端口：25）
 *
 * etang.com:
 * POP3服务器地址:pop.etang.com
 * SMTP服务器地址:smtp.etang.com
 */
public class EmailSender {

    public static Session getSession(){
        Properties props = new Properties();
        try {
            // 开启debug调试
            props.setProperty("mail.debug", "true");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", "smtp.qq.com");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            //SSL认证，腾讯邮箱是基于SSL加密的，所有需要开启才可以使用
            MailSSLSocketFactory sf= new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            //创建会话
            Session session1 = Session.getInstance(props);

            return session1;

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

         return null;
    }

    public static void sendEmail(String MyEmailAddress,String password,String toEmailAddress,String emailTitle,String emailContent){
        try {
            //创建好的会话
            Session session = getSession();

            //将会话放入消息中，该对象是消息服务器在客户端之间发送的一条信息
            //通过message添加需要的信息
            Message msg = new MimeMessage(session);

            //添加邮件标题
            msg.setSubject(emailTitle);

            //在内容结尾添加每次发送的时间
            StringBuilder builder = new StringBuilder();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            builder.append("\n" + simpleDateFormat.format(new Date()));
            builder.append("\n"+emailContent);

            //添加邮件内容
            msg.setText(builder.toString());

            //添加我的邮箱
            msg.setFrom(new InternetAddress(MyEmailAddress));

            //通过session会话创建通知类
            Transport transport = session.getTransport();

            //通过transport连接服务器
            transport.connect("smtp.qq.com", MyEmailAddress, password);

            //通过指定的邮箱地址发送msg里的消息
            transport.sendMessage(msg, new Address[] { new InternetAddress(toEmailAddress) });

            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
   static String MyEmailAddress="1816416284@qq.com";
   static String password="rrhamjkrzcvjdfff";
   static String emailTitle="理博软件账户激活";
    public static void sendQQEmail(String toEmailAddress,String emailContent){
        try {
            //创建好的会话
            Session session = getSession();

            //将会话放入消息中，该对象是消息服务器在客户端之间发送的一条信息
            //通过message添加需要的信息
            Message msg = new MimeMessage(session);

            //添加邮件标题
            msg.setSubject(emailTitle);

            //在内容结尾添加每次发送的时间
            StringBuilder builder = new StringBuilder();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            builder.append("\n"+emailContent);
            builder.append("\n时间:"+simpleDateFormat.format(new Date()));

            //添加邮件内容
            //  msg.setContent(builder.toString(),"text/html;charset=UTF-8");
            msg.setText(builder.toString());

            //添加我的邮箱
            msg.setFrom(new InternetAddress(MyEmailAddress));

            //通过session会话创建通知类
            Transport transport = session.getTransport();

            //通过transport连接服务器
            transport.connect("smtp.qq.com", MyEmailAddress, password);

            //通过指定的邮箱地址发送msg里的消息
            transport.sendMessage(msg, new Address[] { new InternetAddress(toEmailAddress) });

            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void demo(){
         EmailSender.sendQQEmail("1816416284@qq.com","您的用户名是: ,\n您的激活码为,\n您可以点击此链接激活账号 http://localhost:8888/activation.html ");
    }
}

