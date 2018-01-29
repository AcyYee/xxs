package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "person_log_acy")
public class PersonLog {

    @Id
    private String id;

    private String personActive,createTime,personId,commodityId;

    private Integer activeLevel,isDelete,activeType;

}
