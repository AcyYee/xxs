package com.cwy.xxs.service;

import com.cwy.xxs.vo.ResultData;

public interface AdminInfoService {

    /**
     * 管理员登录
     * @param adminName 管理员账号
     * @param adminPassword 管理员密码
     * @param ip 当前访问ip
     * @return 返回记录
     */
    ResultData login(String adminName, String adminPassword, String ip);

    String add(String adminName, String adminPassword);

    ResultData getQRCode(String path);
}
