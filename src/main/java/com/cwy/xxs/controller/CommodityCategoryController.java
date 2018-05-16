package com.cwy.xxs.controller;

import com.cwy.xxs.entity.CommodityCategory;
import com.cwy.xxs.service.CommodityCategoryService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 负责一级二级分类的控制器
 * @author acy19
 * Created by acy on 2017/6/6.
 */
@RestController
@RequestMapping("commodityCategory")
public class CommodityCategoryController {

    private final CommodityCategoryService commodityCategoryService;

    @Autowired
    public CommodityCategoryController(CommodityCategoryService commodityCategoryService) {
        this.commodityCategoryService = commodityCategoryService;
    }


    /**
     * 微信端获取所有一级分类
     * @return 一级分类集
     */
    @RequestMapping("wx/find")
    public ResultData wxFind(HttpServletResponse response, FindModel findModel) {
        return find(response,findModel);
    }

    /**
     * 后台获取所有一级分类
     * @return 一级分类集
     */
    @RequestMapping("find")
    public ResultData find(HttpServletResponse response,@RequestBody FindModel findModel){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityCategoryService.findCategory(findModel);
    }


    /**
     * 微信端获取所有一级分类
     * @return 一级分类集
     */
    @RequestMapping("wx/finds")
    public ResultData wxFinds(HttpServletResponse response, @RequestBody Map<String,Object> map) {
        return finds(response,map);
    }

    /**
     * 后台获取所有一级分类
     * @return 一级分类集
     */
    @RequestMapping("finds")
    public ResultData finds(HttpServletResponse response, @RequestBody Map<String,Object> map){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityCategoryService.getCategorys(map);
    }

    /**
     * 后台添加一级分类
     * @return 添加完成的一级分类
     */
    @RequestMapping("add")
    public ResultData addOne(HttpServletResponse response,@RequestBody CommodityCategory commodityCategory){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityCategoryService.insertCategory(commodityCategory);
    }

    /**
     * 后台批量删除一级分类
     * @param operate 需要删除的数组
     * @return 删除结果
     */
    @RequestMapping("deletes")
    public ResultData deleteOnes(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        List ids = operate.getIds();
        return commodityCategoryService.removeCategorys(ids);
    }

    /**
     * 更新一级分类
     * @param commodityCategory 需要更新的一级分类
     * @return 返回更新结果
     */
    @RequestMapping("update")
    public ResultData updateOne(@RequestBody CommodityCategory commodityCategory, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityCategoryService.updateCategory(commodityCategory);
    }


    /**
     * 排序置换
     */
    @RequestMapping("sort")
    public ResultData sortOne(@RequestBody FindModel findModel , HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return commodityCategoryService.sortCategory(findModel);
    }

}
