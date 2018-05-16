package com.cwy.xxs.config;

import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author acy19
 */
@Component
@ConfigurationProperties(prefix = "wx")
public class WXLoginConfig {

    /**
     * 订单模板信息id
     */
    public static final String ORDER_MESSAGE_ID = "PWZ3FZ4i6O_hRrRTDqfqV6LQrf0CWWN6KHEcNo2MVKc";

    /**
     * 注册信息模板信息id
     */
    public static final String REGISTER_MESSAGE_ID = "dqDH0s2HnEpN-ujihWxDr5y6Eygg9KcpIBE-mOchmRk";

    public static final Logger LOGGER = LoggerFactory.getLogger(WXLoginConfig.class);
    private static String WX_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    private String appid;
    private String secret;
    private String mchId;
    private String mchKey;
    private String appId;
    private String appSecret;

    public static String getAccessToken(WXLoginConfig wxLoginConfig) throws IOException {
        String json = HttpSend.sendGet(wxLoginConfig.getAccessTokenURL(), "");
        HashMap map = JsonUtil.MAPPER.readValue(json, HashMap.class);
        LOGGER.info(map.toString());
        return (String) map.get("access_token");

    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
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

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getMchKey() {
        return mchKey;
    }

    public void setMchKey(String mchKey) {
        this.mchKey = mchKey;
    }

    public String getThisLoginURL(String code) {
        String wxLoginUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        wxLoginUrl = wxLoginUrl.replaceFirst("JSCODE", code);
        wxLoginUrl = wxLoginUrl.replaceFirst("APPID", appid);
        return wxLoginUrl.replaceFirst("SECRET", secret);
    }

    public String getAccessTokenURL() {
        String wxUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        wxUrl = wxUrl.replaceFirst("APPID", appid);
        return wxUrl.replaceFirst("APPSECRET", secret);
    }

    public String getAccessTokenURLOR() {
        String wxUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        wxUrl = wxUrl.replaceFirst("APPID", appId);
        return wxUrl.replaceFirst("APPSECRET", appSecret);
    }

    public String getQRCode(String accessToken) {
        String wxUrl = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=ACCESS_TOKEN";
        return wxUrl.replaceFirst("ACCESS_TOKEN", accessToken);
    }

    public String getMessagePath(String accessToken) {
        String wxUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN";
        return wxUrl.replaceFirst("ACCESS_TOKEN", accessToken);
    }

}
