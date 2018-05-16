package com.cwy.xxs.controller;

import com.cwy.xxs.entity.GroupBuy;
import com.cwy.xxs.service.GroupBuyService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author acy19
 */
@RestController
@RequestMapping("groupBuy")
public class GroupBuyController {

    private final GroupBuyService groupBuyService;

    @Autowired
    public GroupBuyController(GroupBuyService groupBuyService) {
        this.groupBuyService = groupBuyService;
    }

    @PostMapping("add")
    public ResultData add(@RequestBody GroupBuy groupBuy, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return groupBuyService.addGroupBuy(groupBuy);
    }

    @PostMapping("update")
    public ResultData update(@RequestBody GroupBuy groupBuy, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return groupBuyService.updateGroupBuy(groupBuy);
    }

    @PostMapping("deletes")
    public ResultData deletes(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return groupBuyService.deleteGroupBuies(operate);
    }

    @PostMapping("use")
    public ResultData use(@RequestBody Operate operate, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return groupBuyService.useGroupBuies(operate);
    }

    @PostMapping("find")
    public ResultData find(@RequestBody FindModel findModel, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return groupBuyService.findGroupBuy(findModel);
    }

    @PostMapping("wx/finds")
    public ResultData finds(Integer pageIndex,Integer pageSize,String searchString, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        Map<String,Object> map = new HashMap<>(4);
        map.put("pageIndex",pageIndex);
        map.put("pageSize",pageSize);
        map.put("searchString",searchString);
        return groupBuyService.findGroupBuies(map);
    }

    @PostMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return groupBuyService.findGroupBuies(map);
    }

}
