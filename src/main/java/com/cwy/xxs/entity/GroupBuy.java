package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "group_buy_acy")
public class GroupBuy {

    @Id
    private String id;

    private String createTime,updateTime,groupName,commodityId;

    private Double groupPrice;

    private Integer groupNumber,specificationId,isUsed,isDelete,buyType;

    public boolean empty() {
        return specificationId == null || groupPrice == null || groupNumber == null || groupName == null;
    }

    public boolean notEmpty() {
        return id == null || ( groupPrice == null && groupNumber == null && groupName == null);
    }
}
