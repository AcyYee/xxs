package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Document(collection = "group_info_acy")
@Data
public class GroupInfo {

    @Id
    private String id;

    private String buyId,personId,createTime,updateTime,startTime,endTime;

    private Integer isDelete,isUsed,groupType,groupNumber;

}
