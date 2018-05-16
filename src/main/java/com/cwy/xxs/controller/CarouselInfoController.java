package com.cwy.xxs.controller;

import com.cwy.xxs.entity.CarouselInfo;
import com.cwy.xxs.service.CarouselInfoService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author acy19
 */

@RestController
@RequestMapping("carouselInfo")
public class CarouselInfoController {

    private final CarouselInfoService carouselInfoService;

    public CarouselInfoController(CarouselInfoService carouselInfoService) {
        this.carouselInfoService = carouselInfoService;
    }

    /**
     * 添加轮播图
     * @param carouselInfo 轮播图信息
     * @return 返回轮播图添加后的信息
     */
    @RequestMapping("add")
    public ResultData add(@RequestBody CarouselInfo carouselInfo, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return carouselInfoService.addCarouselInfo(carouselInfo);
    }

    /**
     * 更新轮播图信息
     * @param carouselInfo 轮播图信息
     * @return 返回更新后的轮播图信息
     */
    @RequestMapping("update")
    public ResultData update(@RequestBody CarouselInfo carouselInfo,HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return carouselInfoService.updateCarouselInfo(carouselInfo);
    }

    /**
     * 批量删除轮播图信息
     * @param operate 轮播图信息id数组
     * @return 返回删除的轮播图信息数
     */
    @RequestMapping("deletes")
    public ResultData deletes(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        if (operate == null || operate.getIds() == null){
            return ResultData.returnResultData(-1,null);
        }
        return carouselInfoService.deleteByIds(operate.getIds());
    }

    /**
     * 微信获取轮播图信息
     * @param carouselId 轮播图信息id
     * @return 返回轮播图信息
     */
    @RequestMapping("wx/find")
    public ResultData wxFind(String carouselId){
        return carouselInfoService.getCarouselInfo(carouselId);
    }

    /**
     * 微信获取轮播图信息
     * @param findModel 轮播图信息id
     * @return 返回轮播图信息
     */
    @RequestMapping("find")
    public ResultData find(@RequestBody FindModel findModel, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        if (findModel == null || findModel.getId() == null){
            return ResultData.returnResultData(-1,null);
        }
        return carouselInfoService.getCarouselInfo(findModel.getId());
    }

    /**
     * 微信获取轮播图信息列表
     * @param commodityId 商品id
     * @param carouselType 轮播图类型
     * @param pageIndex 页数
     * @param pageSize 每页数量
     * @return 返回数据
     */
    @RequestMapping("wx/finds")
    public ResultData wxFind(Integer commodityId, Integer carouselType, Integer pageIndex , @RequestParam(defaultValue = "10") Integer pageSize, HttpServletResponse response){
        Map<String,Object> map = new HashMap<>();
        map.put("commodityId",commodityId);
        map.put("carouselType",carouselType);
        map.put("pageIndex",pageIndex);
        map.put("pageSize",pageSize);
        return finds(map, response);
    }

    /**
     * 获取轮播图信息列表
     * @param map 商品id
     * @return 返回数据
     */
    @RequestMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return carouselInfoService.getCarouselInfos(map);
    }


}
