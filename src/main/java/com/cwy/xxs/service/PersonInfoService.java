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
     * @return 个人信息
     */
    ResultData wxLogin(String code);
}
