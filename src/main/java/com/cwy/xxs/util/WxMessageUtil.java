package com.cwy.xxs.util;

import com.cwy.xxs.dvo.KeyWord;
import com.cwy.xxs.dvo.WxMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cwy.xxs.config.WXLoginConfig.ORDER_MESSAGE_ID;
import static com.cwy.xxs.util.JsonUtil.MAPPER;

/**
 * @author acy19
 */
public class WxMessageUtil {

    public static boolean sendWxMessage(String url, WxMessage wxMessage){
        String result = HttpSend.sendPostJsonOR(url,wxMessage.toString());
        if ("".equals(result)){
            return false;
        }else {
            try {
                Map map = MAPPER.readValue(result, HashMap.class);
                Integer i = (Integer) map.get("errcode");
                if (i == null || i>0){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    public static void main(String[] args) {

           String fromId = "大萨达所多";
            WxMessage wxMessage = new WxMessage();
            List<KeyWord> keyWords = new ArrayList<>(4);
            keyWords.add(new KeyWord("注册成功领取立减金","#173177"));
            keyWords.add(new KeyWord("叶文雄","#173177"));
            keyWords.add(new KeyWord("1788959437","#173177"));
            keyWords.add(new KeyWord("2018-03-29 13:39:33","#173177"));
            wxMessage.setKeyWords(keyWords);
            wxMessage.setTemplateId(ORDER_MESSAGE_ID);
            wxMessage.setToUser("sdadsadad");
            wxMessage.setFromId(fromId);
        System.out.println(wxMessage.toString());
    }

}