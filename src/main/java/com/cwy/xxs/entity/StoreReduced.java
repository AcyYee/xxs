package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "store_reduced_acy")
@Data
public class StoreReduced {

    @Id
    private String id;

    private Double shouldCount;

    private Double lessCount;

    private Integer isDelete;

    private String storeId;

}