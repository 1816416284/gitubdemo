package com.lening.node;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 阿里云的短信验证码
 */
public class NodeSendInfo {
   //Access密钥id
   static  String accessKeyId="LTAI4GENwRKXb8P7ojCbJwSn";
   //Access密钥密码
   static  String secret = "XL0jCXiMWdP9SczdoNHdKPwz51cycH";

    public static  String NodeSend(String tel,String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret); //密匙id,密匙密码
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", tel);//手机号
        request.putQueryParameter("SignName", "理博软件");  //标签名
        request.putQueryParameter("TemplateCode","SMS_193244354"); //模板id
        request.putQueryParameter("TemplateParam", "{'code':'" + code + "'}");//发送的验证码
        try {
            CommonResponse response = client.getCommonResponse(request);
            return response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String GetBindNum() {
        String[] beforeShuffle = new String[]{
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "a", "b", "d", "c", "e", "f", "g", "h", "i", "j",
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "k", "l", "m", "n", "o", "p", "q", "r", "s", "t",
                "u", "v", "w", "x", "y", "z"};
        List list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(3, 9);
        return result;
    }
    @Test
    public void demo(){
       //  String s = NodeSend("18888832945", "0512Gb");
       //  String s = NodeSendInfo.GetBindNum();
       //   int v = (int)((Math.random() * 9 + 1) * 100000);
    }
}
