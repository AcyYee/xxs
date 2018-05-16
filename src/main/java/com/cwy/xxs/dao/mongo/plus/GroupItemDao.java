package com.cwy.xxs.dao.mongo.plus;


import com.cwy.xxs.entity.GroupItem;

import java.util.List;
import java.util.Map; /**
 * @author acy19
 */
public interface GroupItemDao {

    List<GroupItem> findGroupItems(Map<String, Object> map);


    void updateTypeByGroupId(String groupId, String dateTime,int type);
}
