package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy 屋大维
 */
@Document
@Data
public class UserAddress {

    @Id
    private String id;

    private Integer personId;

    private Integer isDelete;

    private String provinceCode;

    private String addressContent;

    private String addressPhone;

    private String addressPerson;

    private String addressMunicipal;

    private Integer addressType;

    private String addressProvince;

    private String addressCounty;

    private Integer sortNumber;

    private String createTime;

    public boolean notEmpty() {
        return addressContent == null || personId == null || addressPhone == null
                || addressPerson == null || addressMunicipal == null|| addressType == null
                || addressProvince == null || addressCounty == null;
    }

    public boolean empty() {
        return addressContent == null && personId == null && addressPhone == null
                && addressPerson == null && addressMunicipal == null && addressType == null
                && addressProvince == null && addressCounty == null;
    }
}