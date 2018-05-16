package com.cwy.xxs.service.impl;

import com.cwy.xxs.config.WXLoginConfig;
import com.cwy.xxs.dao.mongo.base.PersonInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.PersonLogBaseDao;
import com.cwy.xxs.dao.mongo.base.PhoneCodeBaseDao;
import com.cwy.xxs.dvo.KeyWord;
import com.cwy.xxs.dvo.WxMessage;
import com.cwy.xxs.entity.PersonInfo;
import com.cwy.xxs.entity.PersonLog;
import com.cwy.xxs.entity.PhoneCode;
import com.cwy.xxs.service.PersonInfoService;
import com.cwy.xxs.util.HttpSend;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.util.WxMessageUtil;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cwy.xxs.config.LogConfig.*;
import static com.cwy.xxs.config.PayOrderConfig.ADDRESS_SEND;
import static com.cwy.xxs.config.WXLoginConfig.REGISTER_MESSAGE_ID;
import static com.cwy.xxs.util.JsonUtil.MAPPER;
import static com.cwy.xxs.util.StoreUtil.generateAddress;

/**
 * @author acy19
 * @version 2.0.0
 * 等一个立减金
 */
@Service
public class PersonInfoServiceImpl implements PersonInfoService {

    private final WXLoginConfig wxLoginConfig;

    private final PersonInfoBaseDao personInfoBaseDao;

    private final PhoneCodeBaseDao phoneCodeBaseDao;

    private final PersonLogBaseDao personLogBaseDao;

    private final Logger logger = LoggerFactory.getLogger(PersonInfoServiceImpl.class);

    @Autowired
    public PersonInfoServiceImpl(WXLoginConfig wxLoginConfig, PersonInfoBaseDao personInfoBaseDao, PhoneCodeBaseDao phoneCodeBaseDao, PersonLogBaseDao personLogBaseDao) {
        this.wxLoginConfig = wxLoginConfig;
        this.personInfoBaseDao = personInfoBaseDao;
        this.phoneCodeBaseDao = phoneCodeBaseDao;
        this.personLogBaseDao = personLogBaseDao;
    }

    @Override
    public ResultData wxLogin(String code, String ip) {
        if (code == null) {
            return new ResultData(1002, "陈芽升别他妈乱传东西");
        }
        String url = wxLoginConfig.getThisLoginURL(code);
        String json = HttpSend.sendGet(url, null);
        HashMap result;
        try {
            result = MAPPER.readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultData(1001, "服务器出错");
        }
        logger.info(result.toString());
        String openid = result.get("openid").toString();
        String sessionKey = result.get("session_key").toString();
        if (openid != null && !"".equals(openid)) {
            // 读取或创建用户信息
            PersonInfo personInfo = personLoginChange(openid,sessionKey,ip);
            return new ResultData(2000, "陈芽升赶紧处理数据", personInfo.getId());
        } else {
            return new ResultData(1002, "陈芽升别他妈乱传东西");
        }

    }

    private PersonInfo personLoginChange(String openid,String sessionKey,String ip){
        PersonInfo personInfo;
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        personInfo = personInfoBaseDao.findByOpenid(openid);
        if (personInfo == null) {
            personInfo = new PersonInfo();
            personInfo.setOpenid(openid);
            personInfo.setSessionKey(sessionKey);
            personInfo.setCreateTime(dateTime);
            personInfo.setLastLoginTime(dateTime);
            personInfo.setLastLoginIP(ip);
            personInfo.setVipLevel(0);
            personInfo = personInfoBaseDao.save(personInfo);
            personInfo.setSessionKey(null);
            writeLog(personInfo.getId(), ip, dateTime, LOG_WARING, PERSON_REGISTER, null, "用户注册");
        } else {
            personInfo.setSessionKey(sessionKey);
            personInfo.setLastLoginTime(dateTime);
            personInfo.setLastLoginIP(ip);
            personInfo = personInfoBaseDao.save(personInfo);
            personInfo.setSessionKey(null);
            writeLog(personInfo.getId(), ip, dateTime, LOG_INFO, PERSON_LOGIN, null, "用户登录");
        }
        return personInfo;
    }

    @Override
    public ResultData registerVIP(String id, String userPhone, String phoneCode, String realName, String userAddress, String fromId, String ip) {
        boolean isEmpty = id == null || phoneCode == null || realName == null || userAddress == null;
        if (isEmpty) {
            return new ResultData(1004, "数据有空");
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        PersonInfo personInfo = personInfoBaseDao.findOne(id);
        if (personInfo == null) {
            return new ResultData(1004, "用户信息不存在");
        }
        if (personInfo.getVipLevel() > 0) {
            return ResultData.returnResultData(0, "用户已注册");
        }
        personInfo.setMobilePhone(userPhone);
        personInfo.setUpdateTime(dateTime);
        if (codeIsTrue(personInfo, phoneCode)) {
            String info = generateAddress(ADDRESS_SEND,userPhone,realName,userAddress);
            personInfo.setVipLevel(1);
            personInfo.setUserAddress(info);
            personInfo.setFormId(fromId);
            personInfoBaseDao.save(personInfo);
            writeLog(id, ip, dateTime, LOG_WARING, PERSON_INFO_SET, null, "用户注册会员");
            // TODO 发送立减金
            sendWxMessage(personInfo);
            return new ResultData(2000, "注册成功");
        } else {
            return new ResultData(1004, "验证码有误");
        }
    }

    /**
     * 校验验证码
     *
     * @param personInfo 用户信息
     * @param phoneCode  验证码
     * @return 是否正确
     */
    private boolean codeIsTrue(PersonInfo personInfo, String phoneCode) {
        Sort orders = new Sort(new Sort.Order(Sort.Direction.DESC, "createTime"));
        // 校验验证码
        PhoneCode code;
        if (personInfo.getMobilePhone() != null) {
            code = phoneCodeBaseDao.findByPhoneAndCode(personInfo.getMobilePhone(), phoneCode, personInfo.getUpdateTime(), 3, orders);
        } else {
            code = phoneCodeBaseDao.findByPersonAndCode(personInfo.getId(), phoneCode, personInfo.getUpdateTime(), 3, orders);
        }
        if (code == null) {
            return false;
        }
        code.setIsUsed(1);
        code.setIsDelete(1);
        phoneCodeBaseDao.save(code);
        return true;
    }

    /**
     * 发送微信消息
     *
     * @param personInfo 用户信息
     */
    private void sendWxMessage(PersonInfo personInfo) {
        String fromId = personInfo.getFormId();
        if (fromId != null) {
            String accessToken = null;
            try {
                accessToken = WXLoginConfig.getAccessToken(wxLoginConfig);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            String url = wxLoginConfig.getMessagePath(accessToken);
            WxMessage wxMessage = new WxMessage();
            List<KeyWord> keyWords = new ArrayList<>(4);
            keyWords.add(new KeyWord("注册成功领取立减金", "#173177"));
            keyWords.add(new KeyWord(personInfo.getRealName(), "#173177"));
            keyWords.add(new KeyWord(personInfo.getMobilePhone(), "#173177"));
            keyWords.add(new KeyWord(personInfo.getUpdateTime(), "#173177"));
            wxMessage.setKeyWords(keyWords);
            wxMessage.setTemplateId(REGISTER_MESSAGE_ID);
            wxMessage.setToUser(personInfo.getOpenid());
            wxMessage.setFromId(fromId);
            wxMessage.setPage("/userinfo/userinfo");
            WxMessageUtil.sendWxMessage(url,wxMessage);
        }
    }

    @Override
    public ResultData getVips(Map<String, Object> map) {
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        String searchString = (String) map.get("searchString");
        PageData pageData = new PageData();
        if (pageIndex != null) {
            Pageable pageable;
            if (pageSize == null) {
                pageable = new PageRequest(pageIndex, 15);
            } else {
                pageable = new PageRequest(pageIndex, pageSize);
            }
            pageData.setPageable(pageable);
            if (searchString == null || "".equals(searchString.trim())) {
                pageData.setModelData(personInfoBaseDao.findByVipLevel(pageable));
            } else {
                pageData.setModelData(personInfoBaseDao.findByVipLevel(searchString, pageable));
            }
        } else {
            if (searchString == null || "".equals(searchString.trim())) {
                pageData.setModelData(personInfoBaseDao.findByVipLevel());
            } else {
                pageData.setModelData(personInfoBaseDao.findByVipLevel(searchString));
            }

        }
        return ResultData.returnResultData(1, pageData);

    }

    @Override
    public ResultData wxfind(String id) {
        if (id == null || "".equals(id)) {
            return new ResultData(1004, "数据有空");
        }
        PersonInfo personInfo = personInfoBaseDao.findByIdOR(id);
        if (personInfo == null) {
            return new ResultData(1003, "数据有误");
        }
        return new ResultData(2000, "获取成功", personInfo);
    }

    @Override
    public ResultData setUserInfo(String id, String wxNico, String wxIcon, String wxGender, String wxProvince, String wxCity, String wxCountry, String ip) {
        boolean allow = id == null || (wxCountry == null && wxGender == null && wxIcon == null && wxProvince == null && wxNico == null && wxCity == null);
        if (allow) {
            return new ResultData(1004, "数据有空");
        }
        PersonInfo personInfo = personInfoBaseDao.findOne(id);
        personInfo.setWxNico(wxNico);
        personInfo.setWxIcon(wxIcon);
        personInfo.setWxCity(wxCity);
        personInfo.setWxGender(wxGender);
        personInfo.setWxProvince(wxProvince);
        personInfo.setWxCountry(wxCountry);
        personInfoBaseDao.save(personInfo);
        return new ResultData(2000, "设置成功");
    }

    private void writeLog(String personId, String ip, String dateTime, Integer activeLevel, Integer activeType, String commodityId, String message) {
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

    @Override
    public ResultData find(String id) {
        return ResultData.returnResultData(1, personInfoBaseDao.findOne(id));
    }
}
