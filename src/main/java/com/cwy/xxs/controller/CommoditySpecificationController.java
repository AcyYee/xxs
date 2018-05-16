package com.cwy.xxs.controller;

import com.cwy.xxs.entity.CommoditySpecification;
import com.cwy.xxs.service.CommoditySpecificationService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author acy 屋大维
 */
@RestController
@RequestMapping("commoditySpecification")
public class CommoditySpecificationController {

    private final CommoditySpecificationService commoditySpecificationService;

    private Logger logger = LoggerFactory.getLogger(CommoditySpecificationController.class);

    @Autowired
    public CommoditySpecificationController(CommoditySpecificationService commoditySpecificationService) {
        this.commoditySpecificationService = commoditySpecificationService;
    }

    /**
     * 添加规格
     * @param commoditySpecification 规格信息
     * @return 返回信息
     */
    @RequestMapping("add")
    public ResultData add(@RequestBody CommoditySpecification commoditySpecification, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        logger.info(commoditySpecification.toString());
        return ResultData.returnResultData(commoditySpecificationService.addSpecification(commoditySpecification),commoditySpecification);
    }

    /**
     * 更新规格信息
     * @param commoditySpecification 规格信息
     * @return 返回信息
     */
    @RequestMapping("update")
    public ResultData update(@RequestBody CommoditySpecification commoditySpecification, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return ResultData.returnResultData(commoditySpecificationService.updateSpecification(commoditySpecification),commoditySpecification);
    }

    /**
     * 批量删除规格
     * @param operate 需要批量规格id
     * @return 删除结果
     */
    @RequestMapping("deletes")
    public ResultData deletes(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return ResultData.returnResultData(commoditySpecificationService.deleteSpecifications(operate),null);
    }

    /**
     * 获取规格
     * @param findModel 规格id
     * @return 返回规格
     */
    @RequestMapping("find")
    public ResultData find(@RequestBody FindModel findModel, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        CommoditySpecification commoditySpecification = commoditySpecificationService.getSpecification(findModel.getSpecificationId());
        if (commoditySpecification==null){
            return new ResultData(1004,"数据为空");
        }else {
            return new ResultData(2000,"获取成功",commoditySpecification);
        }
    }

    /**
     * 获取规格
     * @param map 参数
     * @return 返回规格集合
     */
    @RequestMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        PageData pageData = commoditySpecificationService.getSpecifications(map);
        if (pageData == null){
            return new ResultData();
        }else{
            return new ResultData(100,"获取成功",pageData);
        }
    }

    /**
     * 置顶商品
     * @param specificationId 置顶规格id
     * @param sortNumber 置顶规格序号
     * @return 结果
     */
    @RequestMapping("top")
    public ResultData top(Integer specificationId , Integer sortNumber, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return ResultData.returnResultData(commoditySpecificationService.topCommoditySpecification(specificationId,sortNumber),null);
    }

    /**
     * 商品序号
     * @param specificationId 规格id
     * @param sortNumber 序号
     * @return 结果
     */
    @RequestMapping("sort")
    public ResultData sort(Integer specificationId , Integer sortNumber, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return ResultData.returnResultData(commoditySpecificationService.sortCommoditySpecification(specificationId,sortNumber),null);
    }

}
