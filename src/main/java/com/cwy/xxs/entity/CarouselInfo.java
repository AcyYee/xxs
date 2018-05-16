package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "carousel_info_acy")
public class CarouselInfo {

    @Id
    private String id;

    private String imagePath;

    private String storeId;

    private String createTime;

    private String updateTime;

    private Integer sortNumber;

    private String commodityId;

    private Integer isDelete;

    private Integer carouselType;

    public boolean empty() {
        return imagePath == null || carouselType == null || (carouselType == 1 && commodityId == null);
    }

    public boolean notEmpty() {
        return id == null || (imagePath == null && commodityId == null && carouselType ==null);
    }
}