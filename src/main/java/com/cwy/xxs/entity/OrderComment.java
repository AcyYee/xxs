package com.cwy.xxs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author acy19
 */
@Data
@Document(collection = "order_comment_acy")
public class OrderComment {

    @Id
    private String id;

    private String createTime,updateTime,orderId,commentContent,commodityId;

    private Integer itemId,isDelete,commodityComment,allComment,logisticsComment;

    public boolean empty() {
        return itemId == null || commentContent == null || commodityComment == null || allComment == null;
    }
}
