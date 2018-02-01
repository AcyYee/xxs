package com.cwy.xxs.service;

import com.cwy.xxs.vo.ResultData;

/**
 * @author Acy
 */
public interface PhoneCodeService {

    /**
     * 获取微信手机号
     * @param personId 用户id
     * @param iv 加密向量
     * @param encryptedData 加密数据
     * @return 返回成功与否
     */
    ResultData getWXPhone(String personId, String iv, String encryptedData);

    /**
     *  小程序用户发送验证码
     * @param personId 用户id
     * @param phoneNumber 用户手机号
     * @param codeType 验证码类型
     * @return 返回发送结果
     */
    ResultData sendSms(String personId, String phoneNumber, Integer codeType);
}
