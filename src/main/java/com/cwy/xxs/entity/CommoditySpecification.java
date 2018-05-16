package com.cwy.xxs.entity;

import lombok.Data;

/**
 * @author acy 屋大维
 */
@Data
public class CommoditySpecification {

    private Integer specificationId;

    private String specificationName;

    private Integer specificationCount;

    private Double specificationPrice;

    private Integer specificationWeight;

    private Double activityPrice;

    private String imagePath;

    private String commodityId;

    private String createTime;

    private String updateTime;

    private Integer sortNumber;

    private Integer isDelete;

    public boolean empty() {
        return specificationName==null && specificationPrice == null &&  commodityId == null && imagePath == null;
    }

    public boolean notEmpty() {
        return specificationName==null || specificationPrice == null ||  commodityId == null || imagePath == null;
    }
}