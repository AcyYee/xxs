package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Acy
 */
@Document(collection = "admin_log_acy")
@Data
public class AdminLog {

    @Id
    private String id;

    private String adminActive,remoteIP,createTime,adminId,commodityId;

    private Integer activeLevel,isDelete,activeType;

}
