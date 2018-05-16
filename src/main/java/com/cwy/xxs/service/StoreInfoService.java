package com.cwy.xxs.service;

import com.cwy.xxs.entity.StoreInfo;

public interface StoreInfoService {

    StoreInfo getStore(String id);

    int updateStore(StoreInfo storeInfo);

}
