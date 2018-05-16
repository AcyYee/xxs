package com.cwy.xxs.service;

import com.cwy.xxs.vo.ResultData;

import java.util.Map;

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
     * @param fromId 表单id
     * @param ip ip地址
     * @return 返回结果
     */
    ResultData registerVIP(String id, String userPhone, String phoneCode, String realName, String userAddress, String fromId, String ip);

    /**
     * 获取vip用户列表
     * @return 返回vip用户列表
     */
    ResultData getVips(Map<String,Object> map);

    /**
     * 获取信息
     * @param id 用户id
     * @return 返回信息
     */
    ResultData wxfind(String id);

    /**
     * 用户设置信息
     * @param id 用户id
     * @param wxNico 用户昵称
     * @param wxIcon 用户头像
     * @param wxGender 用户性别
     * @param wxProvince 微信省
     * @param ip 当前ip
     * @param wxCity 微信城市
     * @param wxCountry 微信国家
     * @return 返回是否成功
     */
    ResultData setUserInfo(String id, String wxNico, String wxIcon, String wxGender, String wxProvince, String wxCity, String wxCountry,String ip);

    ResultData find(String id);
}
