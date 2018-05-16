package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.StoreInfoBaseDao;
import com.cwy.xxs.entity.StoreInfo;
import com.cwy.xxs.service.StoreInfoService;

import com.cwy.xxs.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author acy 屋大维
 */
@Service("storeInfoService")
public class StoreInfoServiceImpl implements StoreInfoService {

    private final StoreInfoBaseDao storeInfoBaseDao;

    @Autowired
    public StoreInfoServiceImpl(StoreInfoBaseDao storeInfoBaseDao) {
        this.storeInfoBaseDao = storeInfoBaseDao;
    }

    @Override
    public StoreInfo getStore(String id) {
        if (id == null){
            return null;
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        return storeInfoBaseDao.findOne(id);
    }

    @Override
    public int updateStore(StoreInfo storeInfo) {
        if(storeInfo.getId() == null){
            return -1;
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        storeInfo.setCreateTime(dateTime);
        return storeInfoBaseDao.save(storeInfo)==null ? 1:0;
    }


}
