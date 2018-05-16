package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "commodity_info_acy")
public class CommodityInfo {

    @Id
    private String id;

    private String commodityName,subName,createTime,updateTime,commodityPrice,categoryId,imagePath,knowMore,commodityRemark,commodityValues,storeId;

    private Integer isDelete,isGroup,isDiscount,isSales,isActivity,commodityType;

    private Double commodityDiscount;;

    public boolean empty() {
        return  commodityName == null || commodityPrice == null || categoryId==null || imagePath == null ;
    }

}