package com.cwy.xxs.dao.mongo.plus;

import com.cwy.xxs.entity.GroupInfo;

import java.util.List;
import java.util.Map;

public interface GroupInfoDao {

    void updateGroupNumber(String groupId);

    int deleteByIds(List ids);

    List<GroupInfo> findGroupInfoes(Map<String, Object> map);

}
