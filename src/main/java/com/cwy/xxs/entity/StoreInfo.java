package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy 屋大维
 */
@Data
@Document
public class StoreInfo {

    @Id
    private String id;

    private String storeName;

    private String createTime,updateTime;

    private Double storeDiscount;

    private Integer isDiscount;

    private Integer isDelete;

    private Double storeTurnover;

    private String storeLocation;

    private String backgroundImage;

    private String storeImage;

    private Integer maxWeight;

    private Double minMailPrice;

    private Double lessMailPrice;

    private Integer isBusiness;

    private String storeRemark;
    
    private String storeAdmin;
    
    private String storeKey;


    public boolean empty() {
        return storeName == null;
    }

    public boolean notEmpty() {
        return storeName == null;
    }
}