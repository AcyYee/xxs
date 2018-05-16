package com.cwy.xxs.controller;

import com.cwy.xxs.entity.OrderComment;
import com.cwy.xxs.service.OrderCommentService;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author acy19
 */
@RestController
@RequestMapping("orderComment")
public class OrderCommentController {

    private final OrderCommentService orderCommentService;

    public OrderCommentController(OrderCommentService orderCommentService) {
        this.orderCommentService = orderCommentService;
    }

    @PostMapping("wx/add")
    public ResultData add(@RequestBody OrderComment orderComment){
        return orderCommentService.addComment(orderComment);
    }

    @PostMapping("deletes")
    public ResultData deletes(@RequestBody Operate operate){
        return orderCommentService.deleteComments(operate);
    }

    @PostMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map){
        return orderCommentService.findComments(map);
    }

}
