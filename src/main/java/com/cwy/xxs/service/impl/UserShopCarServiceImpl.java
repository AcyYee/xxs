package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.UserShopCarBaseDao;
import com.cwy.xxs.entity.UserShopCar;
import com.cwy.xxs.service.UserShopCarService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.ResultData;
import org.springframework.stereotype.Service;

@Service
public class UserShopCarServiceImpl implements UserShopCarService {

    private final UserShopCarBaseDao userShopCarBaseDao;

    public UserShopCarServiceImpl(UserShopCarBaseDao userShopCarBaseDao) {
        this.userShopCarBaseDao = userShopCarBaseDao;
    }

    @Override
    public ResultData findUserShopCar(FindModel findModel) {
        if (findModel == null || findModel.getId() == null || "".equals(findModel.getId())){
            return ResultData.returnResultData(-1);
        }
        UserShopCar userShopCar = userShopCarBaseDao.findByPersonId(findModel.getId());
        if (userShopCar == null || userShopCar.getId() == null){
            userShopCar = new UserShopCar();
            String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
            userShopCar.setCreateTime(dateTime);
            userShopCar.setUpdateTime(dateTime);
            userShopCar.setPersonId(findModel.getId());
            userShopCar = userShopCarBaseDao.save(userShopCar);
        }
        return ResultData.returnResultData(1,userShopCar);
    }
}
