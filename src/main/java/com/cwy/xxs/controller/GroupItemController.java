package com.cwy.xxs.controller;

import com.cwy.xxs.entity.GroupItem;
import com.cwy.xxs.service.GroupItemService;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("groupItem")
public class GroupItemController {

    private final GroupItemService groupItemService;

    @Autowired
    public GroupItemController(GroupItemService groupItemService) {
        this.groupItemService = groupItemService;
    }

    @PostMapping("wx/finds")
    public ResultData finds(@RequestBody Map<String,Object> map){
        return groupItemService.findGroupItems(map);
    }

    @PostMapping("wx/findORs")
    public ResultData findORs(@RequestBody Map<String,Object> map){
        return groupItemService.findGroupItemORs(map);
    }

}
