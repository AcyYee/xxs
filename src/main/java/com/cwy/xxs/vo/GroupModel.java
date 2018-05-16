package com.cwy.xxs.vo;

import com.cwy.xxs.entity.GroupBuy;
import com.cwy.xxs.entity.GroupInfo;
import com.cwy.xxs.entity.GroupItem;
import lombok.Data;

import java.util.List;

/**
 * @author acy19
 */
@Data
public class GroupModel {

    private GroupInfo groupInfo;

    private GroupBuy groupBuy;

    private List<GroupItem> groupItems;

}
