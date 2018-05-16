package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "commodity_category_acy")
public class CommodityCategory {

    @Id
    private String id;

    private String categoryName,createTime,updateTime,subName,imagePath,iconPath,storeId;

    private Integer sortNumber,isDelete;

    public boolean empty() {
        return categoryName == null;
    }
}