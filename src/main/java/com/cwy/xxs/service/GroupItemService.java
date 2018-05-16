package com.cwy.xxs.service;

import com.cwy.xxs.vo.ResultData;

import java.util.Map;

public interface GroupItemService {
    ResultData findGroupItems(Map<String, Object> map);

    ResultData findGroupItemORs(Map<String, Object> map);
}
