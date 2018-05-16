package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "group_item_acy")
public class GroupItem {

    @Id
    private String id;

    private String nickName,iconPath,createTime,updateTime,groupId,orderId,personId;

    private Integer isDelete,itemType;

}
