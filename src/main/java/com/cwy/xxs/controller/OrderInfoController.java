package com.cwy.xxs.controller;

import com.cwy.xxs.entity.OrderInfo;
import com.cwy.xxs.service.OrderInfoService;
import com.cwy.xxs.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author acy 屋大维
 */

@RestController
@RequestMapping("orderInfo")
public class OrderInfoController {

    private final OrderInfoService orderInfoService;

    @Autowired
    public OrderInfoController(OrderInfoService orderInfoService) {
        this.orderInfoService = orderInfoService;
    }

    /**
     * 单个支付
     * @param orderSubmit 提交信息
     * @return 返回支付数据
     */
    @RequestMapping("wx/addOne")
    public ResultData wxAddOne(@RequestBody OrderSubmit orderSubmit, HttpServletRequest request){
        return orderInfoService.addOneItem(orderSubmit,request.getRemoteAddr());
    }

    /**
     * 购物车支付
     * @param  orderSubmit 提交信息
     * @return 返回支付数据
     */
    @RequestMapping("wx/addAll")
    public ResultData wxAddAll(@RequestBody OrderSubmit orderSubmit, HttpServletRequest request){
        return orderInfoService.addAllItem(orderSubmit, request.getRemoteAddr());
    }


    @RequestMapping("wx/findOR")
    public ResultData wxFindOR(@RequestBody FindModel findModel, HttpServletResponse response){
        return findOR(findModel, response);
    }

    @RequestMapping("wx/deletes")
    public ResultData wxDeletes(Operate operate){
        return ResultData.returnResultData(orderInfoService.userDeletes(operate),null);
    }

    /**
     *  修改订单价格
     * @param map 参数
     * @return 返回处理结果
     *
     */
    @RequestMapping("payManual")
    public ResultData payManual(@RequestBody Map<String,Object> map , HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        return ResultData.returnResultData(orderInfoService.payManual(map),null);
    }

    @RequestMapping("wx/pay")
    public ResultData wxPay(@RequestBody FindModel findModel, HttpServletRequest request){
        return orderInfoService.wxPay(findModel.getOrderId(),request.getRemoteAddr());
    }

    @RequestMapping("send")
    public ResultData send(@RequestBody FindModel findModel){
        return orderInfoService.send(findModel);
    }

    @RequestMapping("wx/receive")
    public ResultData receive(@RequestBody FindModel findModel){
        return orderInfoService.receive(findModel);
    }

    @RequestMapping("wx/cancel")
    public ResultData cancel(@RequestBody FindModel findModel){
        return orderInfoService.cancel(findModel);
    }

    @RequestMapping("findOR")
    public ResultData findOR(@RequestBody FindModel findModel, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        OrderInfoViewModel orderInfo = orderInfoService.findInfoModel(findModel.getOrderId());
        if (orderInfo == null){
            return new ResultData(1004,"数据错误");
        }else {
            return new ResultData(2000,"获取成功",orderInfo);
        }
    }

    @RequestMapping("wx/find")
    public ResultData wxFind(@RequestBody FindModel findModel, HttpServletResponse response){
        return find(findModel, response);
    }

    @RequestMapping("find")
    public ResultData find(@RequestBody FindModel findModel, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        OrderInfo orderInfo = orderInfoService.findInfo(findModel.getOrderId());
        if (orderInfo == null){
            return new ResultData(1004,"数据错误");
        }else {
            return new ResultData(2000,"获取成功",orderInfo);
        }
    }

    @RequestMapping("wx/finds")
    public ResultData wxFinds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        return finds(map, response);
    }

    @RequestMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        PageData pageData = orderInfoService.findInfoes(map);
        if (pageData == null){
            return new ResultData(1004,"数据错误");
        }else {
            return new ResultData(2000,"获取成功",pageData);
        }
    }

    @RequestMapping("wx/findGroupOrder")
    public ResultData findGroupOrder(@RequestBody Map<String,Object> map, HttpServletResponse response){
        response.setHeader("Access-Control-Allow-Origin","*");
        PageData pageData = orderInfoService.findGroupOrders(map);
        if (pageData == null){
            return new ResultData(1004,"数据错误");
        }else {
            return new ResultData(2000,"获取成功",pageData);
        }
    }

}
