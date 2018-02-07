package com.cwy.xxs.service.impl;

import com.cwy.xxs.config.WXLoginConfig;
import com.cwy.xxs.dao.mongo.base.AdminInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.AdminLogBaseDao;
import com.cwy.xxs.entity.AdminInfo;
import com.cwy.xxs.entity.AdminLog;
import com.cwy.xxs.service.AdminInfoService;
import com.cwy.xxs.util.BaseMD5Tools;
import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.util.JsonUtil;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static com.cwy.xxs.config.LogConfig.ADMIN_LOGIN;
import static com.cwy.xxs.config.LogConfig.LOG_INFO;

/**
 * @author Acy
 */
@Service
public class AdminInfoServiceImpl implements AdminInfoService {

    private Logger logger = LoggerFactory.getLogger(AdminInfoServiceImpl.class);

    @Autowired
    private AdminInfoBaseDao adminInfoBaseDao;

    @Autowired
    private WXLoginConfig wxLoginConfig;

    @Autowired
    private AdminLogBaseDao adminLogBaseDao;

    @Override
    public ResultData login(String adminName, String adminPassword, String ip) {
        if (adminName == null || adminPassword == null){
            return new ResultData(1004,"数据错误");
        }
        AdminInfo adminInfo = adminInfoBaseDao.findByNameAndPassword(adminName,adminPassword);
        if (adminInfo == null){
            return new ResultData(1004,"用户名密码错误");
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        adminInfo.setLastLoginIP(ip);
        adminInfo.setLastLoginTime(dateTime);
        writeLog(adminInfo.getId(),ip,dateTime,LOG_INFO,ADMIN_LOGIN,null,"管理员登录");
        return new ResultData(2000,"登录成功",adminInfo);
    }

    private void writeLog(String adminId,String ip,String dateTime,Integer activeLevel,Integer activeType,String commodityId,String message){
        AdminLog adminLog = new AdminLog();
        adminLog.setAdminId(adminId);
        adminLog.setRemoteIP(ip);
        adminLog.setCommodityId(commodityId);
        adminLog.setActiveLevel(activeLevel);
        adminLog.setActiveType(activeType);
        adminLog.setAdminActive(message);
        adminLog.setIsDelete(0);
        adminLog.setCreateTime(dateTime);
        adminLogBaseDao.save(adminLog);
    }

    @Override
    public String add(String adminName, String adminPassword) {
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setAdminName(adminName);
        adminInfo.setAdminPassword(BaseMD5Tools.getMD5(adminPassword));
        adminInfo.setAdminType(1);
        adminInfo.setCreateTime(dateTime);
        adminInfo.setIsDelete(0);
        adminInfoBaseDao.save(adminInfo);
        return adminInfo.toString();
    }

    @Override
    public ResultData getQRCode(String path) {
        String json = HttpSend.sendGet(wxLoginConfig.getAccessTokenURL(),"");
        String accessToken = null;
        try {
            HashMap map = JsonUtil.MAPPER.readValue(json,HashMap.class);
            accessToken = (String) map.get("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String url = wxLoginConfig.getQRCode(accessToken);
        String result = HttpSend.sendPostJson(url, "{\"path\":\""+ path+"\"}");
        try {
            HashMap map = JsonUtil.MAPPER.readValue(result,HashMap.class);
            return new ResultData(2000,"",map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
