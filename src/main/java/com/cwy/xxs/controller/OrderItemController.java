package com.cwy.xxs.controller;

import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.service.OrderItemService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author acy19
 */
@RestController
@RequestMapping("orderItem")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @Autowired
    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @RequestMapping("wx/add")
    public ResultData add(@RequestBody OrderItem orderItem){
        return orderItemService.addOrderItem(orderItem);
    }

    @RequestMapping("wx/update")
    public ResultData update(@RequestBody OrderItem orderItem){
        return ResultData.returnResultData(orderItemService.updateOrderItem(orderItem),orderItem);
    }

    @RequestMapping("wx/deletes")
    public ResultData deletes(@RequestBody Operate operate){
        return ResultData.returnResultData(orderItemService.deleteOrderItems(operate),null);
    }

    @RequestMapping("wx/finds")
    public ResultData finds(@RequestBody Map<String,Object> map){
        PageData pageData = orderItemService.findOrderItems(map);
        if (pageData == null){
            return new ResultData(ResultData.RESULT_ERROR,"数据错误");
        }else{
            return new ResultData(ResultData.RESULT_SUCCESS,"获取成功",pageData);
        }
    }

    @RequestMapping("wx/find")
    public ResultData find(@RequestBody FindModel findModel){
        OrderItem orderItem = orderItemService.findOrderItem(findModel.getItemId());
        if (orderItem == null){
            return new ResultData(ResultData.RESULT_ERROR,"数据错误或无数据");
        }else{
            return new ResultData(ResultData.RESULT_SUCCESS,"获取成功",orderItem);
        }
    }

}
