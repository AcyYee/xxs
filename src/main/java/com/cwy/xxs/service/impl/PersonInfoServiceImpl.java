package com.cwy.xxs.service.impl;

import com.cwy.xxs.config.WXLoginConfig;
import com.cwy.xxs.dao.mongo.base.PersonInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.PersonLogBaseDao;
import com.cwy.xxs.entity.PersonInfo;
import com.cwy.xxs.entity.PersonLog;
import com.cwy.xxs.service.PersonInfoService;
import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.ResultData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author acy19
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private WXLoginConfig wxLoginConfig;

    @Autowired
    private PersonInfoBaseDao personInfoBaseDao;

    private PersonLogBaseDao personLogBaseDao;

    @Override
    public ResultData wxLogin(String code) {
        String url = wxLoginConfig.getThisLoginURL(code);
        String json = HttpSend.sendGet(url,null);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            HashMap result = objectMapper.readValue(json, HashMap.class);
            String openid;
            if ((openid= result.get("openid").toString())!=null){
                PersonInfo personInfo;
                personInfo = personInfoBaseDao.findByOpenid(openid);
                if (personInfo == null) {
                    personInfo = new PersonInfo();
                    personInfo.setOpenid(openid);
                    personInfo.setSessionKey(result.get("session_key").toString());
                    personInfo = personInfoBaseDao.save(personInfo);
                    personInfo.setSessionKey(null);
                }
                String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
                personInfo.setCreateTime(dateTime);
                PersonLog personLog = new PersonLog();
                personLog.setPersonId(personInfo.getId());
                personLog.setCreateTime(dateTime);
                personInfoBaseDao.save(personInfo);
                return new ResultData(2000,"陈芽升赶紧处理数据",personInfo);
            }else {
                return new ResultData(1002,"陈芽升别他妈乱传东西");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultData(1001,"叶文雄快来修代码");
        }
    }
}
