package com.cwy.xxs.util;

/**
 * @author Acy
 */
public class UserPhoneUtil {

    public static String phoneNumber(String phone){
        return phone.substring(0, 3) +
                "****" +
                phone.substring(7, 11);
    }

    /**
     * 生成6位验证码
     * @return 验证码
     */
    public static String getSixCode(){
        return String.valueOf(Math.random()).substring(2, 8);
    }


}
