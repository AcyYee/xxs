package com.cwy.xxs.controller;

import com.cwy.xxs.entity.GroupInfo;
import com.cwy.xxs.service.GroupInfoService;
import com.cwy.xxs.vo.FindModel;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author acy19
 */
@RestController
@RequestMapping("groupInfo")
public class GroupInfoController {

    private final GroupInfoService groupInfoService;

    @Autowired
    public GroupInfoController(GroupInfoService groupInfoService) {
        this.groupInfoService = groupInfoService;
    }

    @PostMapping("find")
    public ResultData find(@RequestBody FindModel findModel){
        return groupInfoService.findGroupInfo(findModel);
    }

    @PostMapping("findModel")
    public ResultData findOR(@RequestBody FindModel findModel){
        return groupInfoService.findGroupInfoModel(findModel);
    }

    @PostMapping("finds")
    public ResultData finds(@RequestBody Map<String,Object> map){
        return groupInfoService.findGroupInfoes(map);
    }

    @PostMapping("deletes")
    public ResultData deletes(@RequestBody Operate operate){
        return groupInfoService.deleteGroupInfoes(operate);
    }

}
