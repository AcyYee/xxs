package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Acy
 */
@Data
@Document(collection = "phone_code_acy")
public class PhoneCode {

    @Id
    private String id;

    private String personId,phoneNumber,resultContent,codeTag,createTime,updateTime,endTime;

    private Integer isSend,codeType,isDelete,isUsed;

}
