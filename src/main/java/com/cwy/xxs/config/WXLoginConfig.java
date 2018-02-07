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

    private String appId;

    private String appSecret;

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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getThisLoginURL(String code){
        String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        wxLoginUrl = wxLoginUrl.replaceFirst("JSCODE",code);
        wxLoginUrl = wxLoginUrl.replaceFirst("APPID",appid);
        return wxLoginUrl.replaceFirst("SECRET",secret);
    }

    public String getAccessTokenURL(){
        String wxUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        wxUrl = wxUrl.replaceFirst("APPID",appId);
        return wxUrl.replaceFirst("APPSECRET",appSecret);
    }

    public String getQRCode(String accessToken){
        String wxUrl = "https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN";
        return wxUrl.replaceFirst("ACCESS_TOKEN",accessToken);
    }

}
