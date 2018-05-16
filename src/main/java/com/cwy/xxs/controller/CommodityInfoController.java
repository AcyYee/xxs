package com.cwy.xxs.controller;

import com.cwy.xxs.entity.CommodityInfo;
import com.cwy.xxs.service.CommodityInfoService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 关于商品信息的所有接口
 * @author acy
 */

@RestController
@RequestMapping("commodityInfo")
public class CommodityInfoController {

    private final CommodityInfoService commodityInfoService;

    @Autowired
    public CommodityInfoController(CommodityInfoService commodityInfoService) {
        this.commodityInfoService = commodityInfoService;
    }

    /**
     * 添加商品数据信息
     * @param commodityInfo 商品数据
     * @return 返回添加后的商品信息数据
     */
    @RequestMapping("add")
    public ResultData add(@RequestBody CommodityInfo commodityInfo, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.addCommodityInfo(commodityInfo);
    }

    /**
     *
     * @param commodityInfo 商品数据
     * @return 返回更新后的商品数据
     */
    @RequestMapping("update")
    public ResultData update(@RequestBody CommodityInfo commodityInfo, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.updateCommodityInfo(commodityInfo);
    }

    /**
     * 微信获取商品列表
     * @param map 查询条件
     * @return 返回集合
     */
    @RequestMapping("wx/finds")
    public ResultData wxFinds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        return finds(map, response);
    }

    /**
     * 获取商品列表
     * @param map 查询条件
     * @return 返回集合
     */
    @RequestMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.findByIds(map);
    }

    /**
     * 获取单个商品
     * @param findModel 商品id
     * @return 结果
     */
    @RequestMapping("wx/find")
    public ResultData wxFind(@RequestBody FindModel findModel, HttpServletResponse response){
        return find(findModel, response);
    }

    /**
     * 获取单个商品
     * @param findModel 商品id
     * @return 结果
     */
    @RequestMapping("find")
    public ResultData find(@RequestBody FindModel findModel, HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.findById(findModel);
    }

    /**
     * 批量删除商品
     * @param operate 需要删除的商品id
     * @return 结果
     */
    @RequestMapping("deletes")
    public ResultData deletes(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.deletesCommodityInfo(operate);
    }


    /**
     * 商品序号
     * @param findModel 商品id
     * @return 结果
     */
    @RequestMapping("sort")
    public ResultData sort(@RequestBody FindModel findModel , HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.sortCommodityInfo(findModel);
    }

    /**
     * 批量开启关闭打折
     * @param operate 需要开启关闭的商品id
     * @return 结果
     */
    @RequestMapping("discounts")
    public ResultData discounts(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.discounts(operate);
    }

    /**
     * 批量开启销售
     @param operate 需要开启关闭的商品id
     * @return 结果
     */
    @RequestMapping("sales")
    public ResultData sales(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityInfoService.sales(operate);
    }

}
