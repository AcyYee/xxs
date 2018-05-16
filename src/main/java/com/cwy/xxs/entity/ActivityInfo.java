package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "activity_info_acy")
public class ActivityInfo {
    @Id
    private String id;

    private String imagePath,createTime,updateTime,activityName,activityContent;

    private Integer isDelete,sortNumber,isActivity,commodityId;

    public boolean unEmpty() {
        return commodityId == null || imagePath == null ;
    }

    public boolean empty(){
        return commodityId == null && imagePath == null ;
    }

}