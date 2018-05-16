package com.cwy.xxs.service.impl;

import com.cwy.xxs.dao.mongo.base.OrderCommentBaseDao;
import com.cwy.xxs.dao.mongo.plus.OrderCommentDao;
import com.cwy.xxs.dao.mybatis.CommoditySpecificationMapper;
import com.cwy.xxs.dao.mybatis.OrderInfoMapper;
import com.cwy.xxs.dao.mybatis.OrderItemMapper;
import com.cwy.xxs.entity.CommoditySpecification;
import com.cwy.xxs.entity.OrderComment;
import com.cwy.xxs.entity.OrderItem;
import com.cwy.xxs.service.OrderCommentService;
import com.cwy.xxs.util.TimeUtil;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.PageData;
import com.cwy.xxs.vo.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.cwy.xxs.config.PayOrderConfig.ORDER_COMMENT_COMPLETE;

/**
 * @author acy19
 */
@Service
public class OrderCommentServiceImpl implements OrderCommentService {

    private final OrderCommentBaseDao orderCommentBaseDao;

    private final OrderCommentDao orderCommentDao;

    private final OrderItemMapper orderItemMapper;

    private final OrderInfoMapper orderInfoMapper;

    private final CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    public OrderCommentServiceImpl(OrderCommentBaseDao orderCommentBaseDao, OrderCommentDao orderCommentDao, OrderItemMapper orderItemMapper, OrderInfoMapper orderInfoMapper, CommoditySpecificationMapper commoditySpecificationMapper) {
        this.orderCommentBaseDao = orderCommentBaseDao;
        this.orderCommentDao = orderCommentDao;
        this.orderItemMapper = orderItemMapper;
        this.orderInfoMapper = orderInfoMapper;
        this.commoditySpecificationMapper = commoditySpecificationMapper;
    }

    @Override
    public ResultData findComments(Map<String, Object> map) {
        PageData pageData = new PageData();
        Integer pageIndex,pageSize;
        pageIndex = (Integer) map.get("pageIndex");
        pageSize = (Integer) map.get("pageSize");
        if (pageIndex != null) {
            Pageable pageable;
            Sort sort = new Sort(Sort.Direction.DESC,"id");
            if (pageSize == null) {
                pageable = new PageRequest(pageIndex, 15, sort);
            }else{
                pageable = new PageRequest(pageIndex, pageSize, sort);
            }
            map.put("pageable",pageable);
            pageData.setPageable(pageable);
            pageData.setModelData(orderCommentDao.findOrderComments(map));
        }else {
            pageData.setModelData(orderCommentDao.findOrderComments(map));
        }
        return ResultData.returnResultData(1,pageData);
    }

    @Override
    public ResultData deleteComments(Operate operate) {
        List ids = null;
        if (operate != null){
            ids = operate.getIds();
        }
        if (ids == null ||ids.size()<1 ) {
            return ResultData.returnResultData(-1,null);
        }else {
            return ResultData.returnResultData(orderCommentDao.deleteByIds(ids));
        }
    }

    @Override
    public ResultData addComment(OrderComment orderComment) {
        if (orderComment == null || orderComment.empty()){
            return ResultData.returnResultData(-1);
        }else {
            OrderComment temp = orderCommentBaseDao.findByItemId(orderComment.getItemId());
            if (temp != null){
                return ResultData.returnResultData(0,"已评论");
            }
            OrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderComment.getItemId());
            CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId());
            String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
            orderComment.setCreateTime(dateTime);
            orderComment.setOrderId(orderItem.getOrderId());
            orderComment.setCommodityId(commoditySpecification.getCommodityId());
            orderComment.setUpdateTime(dateTime);
            orderComment.setIsDelete(0);
            orderInfoMapper.updateTypeById(orderItem.getOrderId(),dateTime,ORDER_COMMENT_COMPLETE);
            return ResultData.returnResultData(1,orderCommentBaseDao.save(orderComment));
        }
    }
}
