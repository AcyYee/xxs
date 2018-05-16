package com.cwy.xxs.controller;

import com.cwy.xxs.entity.StoreInfo;
import com.cwy.xxs.service.StoreInfoService;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author acy 屋大维
 */
@RestController
@RequestMapping("store/info")
public class StoreInfoController {

    private final StoreInfoService storeInfoService;

    @Autowired
    public StoreInfoController(StoreInfoService storeInfoService) {
        this.storeInfoService = storeInfoService;
    }

    @RequestMapping("wx/find")
    public ResultData wxGets(String id, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        return get(id,response);
    }

    @RequestMapping("find")
    public ResultData get(String id, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        StoreInfo storeInfo =storeInfoService.getStore(id);
        if (storeInfo != null){
            return new ResultData(ResultData.RESULT_SUCCESS,"获取成功",storeInfo);
        }else{
            return new ResultData();
        }
    }


	@RequestMapping("update")
    public ResultData update(@RequestBody StoreInfo storeInfo, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        return ResultData.returnResultData(storeInfoService.updateStore(storeInfo),storeInfo);
    }

}
