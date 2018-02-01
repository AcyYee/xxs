package com.cwy.xxs.service;

import com.cwy.xxs.vo.ResultData;

/**
 * @author acy
 * @version 1.0
 * 个人信息的业务逻辑
 */
public interface PersonInfoService {
    /**
     * 登录操作
     * @param code 小程序登录时的code
     * @param ip 登录ip
     * @return 个人信息
     */
    ResultData wxLogin(String code, String ip);

    /**
     * 注册为会员
     *
     * @param id 用户id
     * @param userPhone 用户手机号
     * @param phoneCode 手机验证码
     * @param realName 真实名称
     * @param userAddress 用户地址
     * @param ip ip地址
     * @return 返回结果
     */
    ResultData registerVIP(String id, String userPhone, String phoneCode, String realName, String userAddress, String ip);

    /**
     * 获取信息
     * @param id 用户id
     * @return 返回信息
     */
    ResultData wxfind(String id);

    /**
     *
     * @param id
     * @param wxNico
     * @param wxIcon
     * @param wxGender
     * @param wxProvince
     * @param wxProvince1
     * @param wxCity
     * @param wxCountry
     * @return
     */
    ResultData setUserInfo(String id, String wxNico, String wxIcon, String wxGender, String wxProvince, String wxProvince1, String wxCity, String wxCountry);
}
