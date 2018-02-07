package com.cwy.xxs.service.impl;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;

import com.cwy.xxs.config.AliyunMS;
import com.cwy.xxs.dao.mongo.base.PersonInfoBaseDao;
import com.cwy.xxs.dao.mongo.base.PhoneCodeBaseDao;
import com.cwy.xxs.entity.PersonInfo;
import com.cwy.xxs.entity.PhoneCode;
import com.cwy.xxs.service.PhoneCodeService;
import com.cwy.xxs.util.JsonUtil;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.util.WXDecrypt;
import com.cwy.xxs.vo.ResultData;
import com.cwy.xxs.vo.WXUserPhone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.cwy.xxs.util.JsonUtil.MAPPER;

/**
 * @author Acy
 */

@Service
public class PhoneCodeServiceImpl implements PhoneCodeService {

    @Autowired
    private PhoneCodeBaseDao phoneCodeBaseDao;

    @Autowired
    private PersonInfoBaseDao personInfoBaseDao;

    @Override
    public ResultData getWXPhone(String id, String iv, String encryptedData) {
        if (iv == null || encryptedData == null){
            return new ResultData(1002,"陈芽升别他妈乱传东西");
        }
        PersonInfo personInfo = personInfoBaseDao.findOne(id);
        String sessionKey = personInfo.getSessionKey();
        byte[] result = WXDecrypt.decrypt(sessionKey,iv,encryptedData);
        if (result == null){
            return new ResultData(1003,"解密失败");
        }
        String json = new String(result);
        WXUserPhone userPhone;
        try {
            userPhone = MAPPER.readValue(json,WXUserPhone.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResultData(1001,"解码失败");
        }
        if (userPhone == null || "".equals(userPhone.getPhoneNumber())){
            return new ResultData(1004,"数据缺失");
        }else {
            return sendSms(id,userPhone.getPhoneNumber(),3);
        }
    }

    @Override
    public ResultData sendSms(String personId, String phoneNumber, Integer codeType) {
        if (personId == null || codeType == null){
            return new ResultData(1002,"陈芽升别他妈乱传东西");
        }
        if (phoneNumber == null && codeType == 3){
            return new ResultData(1002,"陈芽升别他妈乱传东西");
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        PhoneCode phoneCode = new PhoneCode();
        phoneCode.setCodeType(codeType);
        phoneCode.setCreateTime(dateTime);
        phoneCode.setPersonId(personId);
        phoneCode.setIsDelete(0);
        phoneCode.setIsUsed(0);
        phoneCode.setIsSend(0);
        phoneCode.setEndTime(TimeUtil.getDateAddMinute(dateTime,3, 1));
        phoneCode.setCodeTag(AliyunMS.getSixCode());
        if (phoneNumber == null){
            PersonInfo personInfo = personInfoBaseDao.findOne(personId);
            if (personInfo == null){
                return new ResultData(1002,"信息出错");
            }else if(personInfo.getMobilePhone() == null){
                return new ResultData(1002,"暂无手机号");
            }else {
                phoneCode.setPhoneNumber(personInfo.getMobilePhone());
            }
        }else{
            phoneCode.setPhoneNumber(phoneNumber);
        }
        SendSmsResponse sendSmsResponse;
        try {
            sendSmsResponse = AliyunMS.sendSms(phoneCode.getPhoneNumber(),phoneCode.getCodeTag());
            if (sendSmsResponse == null || !"OK".equals(sendSmsResponse.getCode())){
                phoneCode.setIsDelete(1);
                phoneCode.setResultContent(JsonUtil.objectToJson(sendSmsResponse));
                return new ResultData(1001,"发送失败",null);
            }else {
                phoneCode.setIsSend(1);
                phoneCode.setResultContent(JsonUtil.objectToJson(sendSmsResponse));
            }
        } catch (ClientException e) {
            e.printStackTrace();
            phoneCode.setIsDelete(1);
            phoneCode.setResultContent("网络错误");
            return new ResultData(1001,"网络出错",null);
        } finally {
            phoneCodeBaseDao.save(phoneCode);
        }
        return new ResultData(2000,"发送成功",phoneNumber);
    }
}
