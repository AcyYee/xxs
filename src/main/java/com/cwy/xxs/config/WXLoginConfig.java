package com.cwy.xxs.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author acy19
 */
@Component
@ConfigurationProperties(prefix = "wx")
public class WXLoginConfig {

    private String appid;

    private String secret;

    private static String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public  String getSecret() {
        return secret;
    }

    public  void setSecret(String secret) {
        this.secret = secret;
    }

    public String getThisLoginURL(String code){
        String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        wxLoginUrl = wxLoginUrl.replaceFirst("JSCODE",code);
        wxLoginUrl = wxLoginUrl.replaceFirst("APPID",appid);
        return wxLoginUrl.replaceFirst("SECRET",secret);
    }

}
