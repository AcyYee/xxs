package com.cwy.xxs.service.impl;

import com.cwy.xxs.config.WXLoginConfig;
import com.cwy.xxs.dao.mongo.base.PersonInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.PersonLogBaseDao;
import com.cwy.xxs.dao.mongo.base.PhoneCodeBaseDao;
import com.cwy.xxs.entity.PersonInfo;
import com.cwy.xxs.entity.PersonLog;
import com.cwy.xxs.entity.PhoneCode;
import com.cwy.xxs.service.PersonInfoService;
import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

import static com.cwy.xxs.config.LogConfig.*;
import static com.cwy.xxs.util.JsonUtil.MAPPER;

/**
 * @author acy19
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    @Autowired
    private WXLoginConfig wxLoginConfig;

    @Autowired
    private PersonInfoBaseDao personInfoBaseDao;

    @Autowired
    private PhoneCodeBaseDao phoneCodeBaseDao;

    @Autowired
    private PersonLogBaseDao personLogBaseDao;

    private final Logger logger = LoggerFactory.getLogger(PersonInfoServiceImpl.class);

    @Override
    public ResultData wxLogin(String code, String ip) {
        if (code == null){
            return new ResultData(1002,"陈芽升别他妈乱传东西");
        }
        String url = wxLoginConfig.getThisLoginURL(code);
        String json = HttpSend.sendGet(url,null);
        HashMap result;
        try {
            result = MAPPER.readValue(json,HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultData(1001,"服务器出错");
        }
        String openid;
            if ((openid= result.get("openid").toString())!=null){
                PersonInfo personInfo;
                String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
                personInfo = personInfoBaseDao.findByOpenid(openid);
                if (personInfo == null) {
                    personInfo = new PersonInfo();
                    personInfo.setOpenid(openid);
                    personInfo.setSessionKey(result.get("session_key").toString());
                    personInfo.setCreateTime(dateTime);
                    personInfo.setLastLoginTime(dateTime);
                    personInfo.setLastLoginIP(ip);
                    personInfo.setVipLevel(0);
                    personInfo = personInfoBaseDao.save(personInfo);
                    personInfo.setSessionKey(null);
                    writeLog(personInfo.getId(),ip,dateTime,LOG_WARING,PERSON_REGISTER,null,"用户注册");
                }else {
                    personInfo.setSessionKey(result.get("session_key").toString());
                    personInfo.setLastLoginTime(dateTime);
                    personInfo.setLastLoginIP(ip);
                    personInfo = personInfoBaseDao.save(personInfo);
                    personInfo.setSessionKey(null);
                }
                writeLog(personInfo.getId(),ip,dateTime,LOG_INFO,PERSON_LOGIN,null,"用户登录");
                return new ResultData(2000,"陈芽升赶紧处理数据",personInfo.getId());
            }else {
                return new ResultData(1002,"陈芽升别他妈乱传东西");
            }

    }

    @Override
    public ResultData registerVIP(String id, String userPhone, String phoneCode, String realName, String userAddress, String ip) {
        boolean isEmpty = id == null || phoneCode==null || realName ==null || userAddress == null;
        if(isEmpty){
            return new ResultData(1004,"数据有空");
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        Sort orders = new Sort(new Sort.Order(Sort.Direction.DESC,"createTime"));
        PhoneCode code;
        String info = id + userPhone+dateTime+realName+userAddress;
        logger.info(info);
        if(userPhone != null) {
            code = phoneCodeBaseDao.findByPhoneAndCode(userPhone, phoneCode, dateTime,3, orders);
        }else {
            code = phoneCodeBaseDao.findByPersonAndCode(id, phoneCode, dateTime,3 ,orders);
        }
        if (code == null){
            return new ResultData(1004,"验证码有误");
        }
        PersonInfo personInfo = personInfoBaseDao.findOne(id);
        if (personInfo == null){
            return new ResultData(1004,"用户信息不存在");
        }
        code.setIsUsed(1);
        code.setIsDelete(1);
        phoneCodeBaseDao.save(code);
        personInfo.setMobilePhone(code.getPhoneNumber());
        personInfo.setVipLevel(1);
        personInfo.setUpdateTime(dateTime);
        personInfo.setUserAddress(userAddress);
        personInfoBaseDao.save(personInfo);
        writeLog(id,ip,dateTime,LOG_WARING,PERSON_INFO_SET,null,"用户注册会员");
        return new ResultData(2000,"注册成功");
    }

    @Override
    public ResultData wxfind(String id) {
        if (id== null || "".equals(id)){
            return new ResultData(1004,"数据有空");
        }
        PersonInfo personInfo = personInfoBaseDao.findByIdOR(id);
        if(personInfo == null){
            return new ResultData(1003,"数据有误");
        }
        return new ResultData(2000,"获取成功",personInfo);
    }

    @Override
    public ResultData setUserInfo(String id, String wxNico, String wxIcon, String wxGender, String wxProvince, String wxCity, String wxCountry,String ip) {
        boolean allow = id == null || (wxCountry==null&&wxGender==null&& wxIcon == null && wxProvince==null&& wxNico==null &&wxCity==null);
        if (allow){
            return new ResultData(1004,"数据有空");
        }
        PersonInfo personInfo = personInfoBaseDao.findOne(id);
        personInfo.setWxNico(wxNico);
        personInfo.setWxIcon(wxIcon);
        personInfo.setWxCity(wxCity);
        personInfo.setWxGender(wxGender);
        personInfo.setWxProvince(wxProvince);
        personInfo.setWxCountry(wxCountry);
        personInfoBaseDao.save(personInfo);
        return new ResultData(2000,"设置成功");
    }

    private void writeLog(String personId, String ip, String dateTime, Integer activeLevel, Integer activeType, String commodityId, String message){
        PersonLog personLog = new PersonLog();
        personLog.setPersonId(personId);
        personLog.setRemoteIP(ip);
        personLog.setActiveLevel(activeLevel);
        personLog.setActiveType(activeType);
        personLog.setPersonActive(message);
        personLog.setCommodityId(commodityId);
        personLog.setIsDelete(0);
        personLog.setCreateTime(dateTime);
        personLogBaseDao.save(personLog);
    }

}
