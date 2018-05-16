package com.cwy.xxs.service.impl;

import com.cwy.xxs.config.WXLoginConfig;
import com.cwy.xxs.dao.mongo.base.*;
import com.cwy.xxs.dao.mongo.plus.GroupItemDao;
import com.cwy.xxs.dao.mybatis.CommoditySpecificationMapper;
import com.cwy.xxs.dao.mybatis.OrderInfoMapper;
import com.cwy.xxs.dao.mybatis.OrderItemMapper;
import com.cwy.xxs.dvo.AbstractWxPayDo;
import com.cwy.xxs.dvo.KeyWord;
import com.cwy.xxs.dvo.WxMessage;
import com.cwy.xxs.entity.*;
import com.cwy.xxs.service.OrderInfoService;
import com.cwy.xxs.util.*;
import com.cwy.xxs.util.wxpaysdk.WXPayUtil;
import com.cwy.xxs.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.cwy.xxs.config.PayOrderConfig.*;
import static com.cwy.xxs.config.WXLoginConfig.ORDER_MESSAGE_ID;
import static com.cwy.xxs.util.StoreUtil.updatePersonVipGrade;
import static com.cwy.xxs.util.wxpaysdk.WXPayConstants.SUCCESS;

/**
 * @author acy 屋大维
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    private final Logger logger = LoggerFactory.getLogger(OrderInfoServiceImpl.class);

    private final AdminInfoBaseDao adminInfoBaseDao;

    private final WXLoginConfig wxLoginConfig;

    private final OrderInfoMapper orderInfoMapper;

    private final OrderItemMapper orderItemMapper;

    private final PersonInfoBaseDao personInfoBaseDao;

    private final CommodityInfoBaseDao commodityInfoBaseDao;

    private final GroupBuyBaseDao groupBuyBaseDao;

    private final GroupInfoBaseDao groupInfoBaseDao;

    private final GroupItemBaseDao groupItemBaseDao;

    private final GroupItemDao groupItemDao;

    private final CommoditySpecificationMapper commoditySpecificationMapper;

    @Autowired
    public OrderInfoServiceImpl(
            AdminInfoBaseDao adminInfoBaseDao, WXLoginConfig wxLoginConfig, OrderInfoMapper orderInfoMapper,
            OrderItemMapper orderItemMapper, PersonInfoBaseDao personInfoBaseDao,
            CommodityInfoBaseDao commodityInfoBaseDao, GroupBuyBaseDao groupBuyBaseDao,
            GroupInfoBaseDao groupInfoBaseDao, GroupItemBaseDao groupItemBaseDao,
            GroupItemDao groupItemDao, CommoditySpecificationMapper commoditySpecificationMapper) {
        this.adminInfoBaseDao = adminInfoBaseDao;
        this.wxLoginConfig = wxLoginConfig;
        this.orderInfoMapper = orderInfoMapper;
        this.orderItemMapper = orderItemMapper;
        this.personInfoBaseDao = personInfoBaseDao;
        this.commodityInfoBaseDao = commodityInfoBaseDao;
        this.groupBuyBaseDao = groupBuyBaseDao;
        this.groupInfoBaseDao = groupInfoBaseDao;
        this.groupItemBaseDao = groupItemBaseDao;
        this.groupItemDao = groupItemDao;
        this.commoditySpecificationMapper = commoditySpecificationMapper;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    @Override
    public ResultData addOneItem(OrderSubmit orderSubmit, String ip) {
        String storeId = orderSubmit.getStoreId();
        String groupBuyId = orderSubmit.getBuyId();
        Integer itemCount = orderSubmit.getItemCount();
        Integer specificationId = orderSubmit.getSpecificationId();
        Integer itemType = orderSubmit.getItemType();
        Integer addressType = orderSubmit.getAddressType();
        Integer orderCategory = orderSubmit.getOrderCategory();
        String personId = orderSubmit.getPersonId();
        String addressInfo = orderSubmit.getAddressInfo();
        String orderRemark = orderSubmit.getOrderRemark();
        boolean isOk = personId == null || specificationId == null || itemCount == null || orderCategory == null || itemType == null || addressType == null || (addressType == 3 && addressInfo == null);
        //判断
        if (isOk) {
            return ResultData.returnResultData(-1, null);
        }
        //获取信息
        CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(orderSubmit.getSpecificationId());
        CommodityInfo commodityInfo = commodityInfoBaseDao.findOne(commoditySpecification.getCommodityId());
        PersonInfo personInfo = personInfoBaseDao.findOne(personId);
        //获取当前时间
        String createTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        OrderInfo orderInfo = new OrderInfo();
        OrderItem orderItem = new OrderItem();
        //设置订单项信息
        orderItem.setItemCount(itemCount);
        orderItem.setItemType(itemType);
        orderItem.setSpecificationId(specificationId);
        orderItem.setCreateTime(createTime);
        orderItem.setUpdateTime(createTime);
        //设置订单信息
        orderInfo.setPersonId(personId);
        orderInfo.setCreateTime(createTime);
        orderInfo.setUpdateTime(createTime);
        orderInfo.setStoreId(storeId);
        orderInfo.setIsDelete(COMMON_NOT);
        orderInfo.setOrderRemark(orderRemark);
        orderInfo.setOrderCategory(orderCategory);
        orderInfo.setOrderId(UUIDUtill.getOrderTag("xxs"));
        orderInfo.setAddressType(addressType);
        orderInfo.setOrderType(ORDER_UN_PAY);
        if (setAddress(orderSubmit, addressType, personInfo, orderInfo)) {
            return ResultData.returnResultData(-1);
        }
        if (orderInfoMapper.insertSelective(orderInfo) > 0) {
            //判定是否为团购
            if (orderCategory == ORDER_GROUP_OPEN || orderCategory == ORDER_GROUP_JOIN) {
                GroupBuy groupBuy;
                GroupInfo groupInfo;
                //只允许购买一件
                orderItem.setItemCount(1);
                if (orderCategory == ORDER_GROUP_OPEN) {
                    groupBuy = groupBuyBaseDao.findOne(groupBuyId);
                    commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(groupBuy.getSpecificationId());
                    groupInfo = new GroupInfo();
                    groupInfo.setPersonId(personId);
                    groupInfo.setBuyId(groupBuyId);
                    groupInfo.setIsDelete(COMMON_NOT);
                    groupInfo.setCreateTime(createTime);
                    groupInfo.setIsUsed(GROUP_INFO_CREATE);
                    groupInfo.setUpdateTime(createTime);
                    groupInfo.setGroupNumber(0);
                    groupInfo.setGroupType(GROUP_INFO_CREATE);
                    groupInfo = groupInfoBaseDao.save(groupInfo);

                } else {
                    groupInfo = groupInfoBaseDao.findOne(orderSubmit.getGroupId());
                    groupBuy = groupBuyBaseDao.findOne(groupInfo.getBuyId());
                    Integer count = groupItemBaseDao.findByPersonIdAndGroupId(personId, groupInfo.getId());
                    if (count < 1 || groupBuy.getGroupNumber() > groupInfo.getGroupNumber() || TimeUtil.comparingDate(createTime, groupInfo.getEndTime())) {
                        commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(groupBuy.getSpecificationId());
                    } else {
                        List<Integer> item = new ArrayList<>(2);
                        item.add(orderItem.getItemId());
                        orderItemMapper.deleteByIds(item);
                        List<String> info = new ArrayList<>(2);
                        info.add(orderInfo.getOrderId());
                        orderInfoMapper.deleteByIds(info);
                        return new ResultData(1005, "人数已满");
                    }
                }
                orderInfo.setGroupId(groupInfo.getId());
                orderItem.setItemPrice(groupBuy.getGroupPrice());
                orderItem.setItemWeight(commoditySpecification.getSpecificationWeight() * orderItem.getItemCount());
            }
            //单独购买
            else if (orderCategory == ORDER_ONE_COMMON) {
                if (commodityInfo.getIsActivity() == COMMON_IS) {
                    orderItem.setItemPrice(orderItem.getItemCount() * commoditySpecification.getActivityPrice());
                    orderItem.setItemWeight(commoditySpecification.getSpecificationWeight() * orderItem.getItemCount());
                } else {
                    orderItem.setItemPrice(orderItem.getItemCount() * commoditySpecification.getSpecificationPrice());
                    orderItem.setItemWeight(commoditySpecification.getSpecificationWeight() * orderItem.getItemCount());
                }
                if (commodityInfo.getIsDiscount() == COMMON_IS) {
                    orderItem.setItemPrice(orderItem.getItemPrice() * commodityInfo.getCommodityDiscount());
                }
            } else {
                return ResultData.returnResultData(-1);
            }
            orderItem.setOrderId(orderInfo.getOrderId());
            orderInfo.setAllowPrice(orderItem.getItemPrice());
            orderInfo.setPayPrice(orderItem.getItemPrice());
            orderItemMapper.insert(orderItem);
            orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
            String orderPrice = String.valueOf(Math.round(orderInfo.getPayPrice() * 100));
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //关闭订单
                    closeOrderInfo(orderInfo.getOrderId());
                }
            }, TimeUtil.getDateAddMinute(createTime, 120));
            return pay(orderInfo, orderPrice, personInfo, ip);
        } else {
            return ResultData.returnResultData(0, 3);
        }
    }

    private boolean setAddress(OrderSubmit orderSubmit, Integer addressType, PersonInfo personInfo, OrderInfo orderInfo) {
        orderInfo.setAddressInfo(orderSubmit.getAddressInfo());
        if (addressType == ADDRESS_SEND) {
            orderInfo.setAddressInfo(personInfo.getUserAddress());
        } else if (addressType == ADDRESS_SELF) {
            //TODO
            orderInfo.setAddressInfo(personInfo.getUserAddress());
        } else if (addressType == ADDRESS_WX) {
            orderInfo.setAddressInfo(orderSubmit.getAddressInfo());
        } else {
            return true;
        }
        return false;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    @Override
    public ResultData addAllItem(OrderSubmit orderSubmit, String ip) {
        List<Integer> itemIds = orderSubmit.getItemIds();
        String personId = orderSubmit.getPersonId();
        String orderRemark = orderSubmit.getOrderRemark();
        String storeId = orderSubmit.getStoreId();
        Integer addressType = orderSubmit.getAddressType();
        String addressInfo = orderSubmit.getAddressInfo();
        boolean isOk = itemIds == null || itemIds.size() < 1 || addressType == null || personId == null || (addressType == 3 && addressInfo == null);
        //判断
        if (isOk) {
            return ResultData.returnResultData(-1, null);
        }
        //获取信息
        PersonInfo personInfo = personInfoBaseDao.findOne(personId);
        String createTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        OrderInfo orderInfo = new OrderInfo();
        //设置订单信息
        orderInfo.setPersonId(personId);
        orderInfo.setCreateTime(createTime);
        orderInfo.setUpdateTime(createTime);
        orderInfo.setStoreId(storeId);
        orderInfo.setIsDelete(COMMON_NOT);
        orderInfo.setOrderRemark(orderRemark);
        orderInfo.setOrderCategory(ORDER_SHOP_CAR);
        orderInfo.setOrderId(UUIDUtill.getOrderTag("xxs"));
        orderInfo.setAddressType(addressType);
        orderInfo.setOrderType(ORDER_UN_PAY);
        if (setAddress(orderSubmit, addressType, personInfo, orderInfo)) {
            return ResultData.returnResultData(-1);
        }
        orderInfo.setAllowPrice(0.00);
        if (orderInfoMapper.insertSelective(orderInfo) > 0) {
            for (Integer itemId : itemIds) {
                OrderItem orderItem = orderItemMapper.selectByPrimaryKey(itemId);
                CommoditySpecification commoditySpecification = commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId());
                CommodityInfo commodityInfo = commodityInfoBaseDao.findOne(commoditySpecification.getCommodityId());
                if (commodityInfo.getIsActivity() == 1) {
                    orderItem.setItemPrice(orderItem.getItemCount() * commoditySpecification.getActivityPrice());
                } else {
                    orderItem.setItemPrice(orderItem.getItemCount() * commoditySpecification.getSpecificationPrice());
                }
                orderItem.setItemWeight(orderItem.getItemCount() * commoditySpecification.getSpecificationWeight());
                if (commodityInfo.getIsDiscount() == 1) {
                    orderItem.setItemPrice(orderItem.getItemPrice() * commodityInfo.getCommodityDiscount());
                }
                orderInfo.setAllowPrice(orderInfo.getAllowPrice() + orderItem.getItemPrice());
                orderItem.setOrderId(orderInfo.getOrderId());
                orderItem.setShopCarId("");
                orderItem.setItemType(ITEM_ORDER);
                orderItemMapper.updateByPrimaryKeySelective(orderItem);
            }
            //计算结束>
            orderInfo.setPayPrice(orderInfo.getAllowPrice());
            String orderPrice = String.valueOf(Math.round(orderInfo.getPayPrice() * 100));
            orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //关闭订单
                    closeOrderInfo(orderInfo.getOrderId());
                }
            }, TimeUtil.getDateAddMinute(createTime, 120));
            return pay(orderInfo, orderPrice, personInfo, ip);
        } else {
            return ResultData.returnResultData(0, null);
        }

    }

    // 完成逻辑
    private void closeOrderInfo(String orderId) {
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        orderInfo.setIsDelete(1);
        orderInfo.setUpdateTime(dateTime);
        orderInfo.setOrderType(ORDER_NOT_ALLOW);
        orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
    }

    // 完成逻辑
    private ResultData pay(OrderInfo orderInfo, String orderPrice, PersonInfo personInfo, String ip) {
        if (orderInfoMapper.updateByPrimaryKeySelective(orderInfo) > 0) {
            //吊起支付
            Map<String, String> result = WXpaySDKUtil.payRequestSmallProgram("先小生订单", orderPrice, personInfo.getOpenid(), ip, orderInfo.getOrderId());
            if (result == null) {
                return ResultData.returnResultData(-1);
            }
            orderInfo.setPrepayId(result.get("prepay_id"));
            orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
            return ResultData.returnResultData(1, result);
        } else {
            return ResultData.returnResultData(0, 4);
        }
    }

    @Override
    public OrderInfo findInfo(String orderId) {
        if (orderId == null) {
            return null;
        }
        return orderInfoMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public PageData findInfoes(Map<String, Object> map) {

        List<OrderInfoViewListModel> listModels = new ArrayList<>();
        PageData pageData = new PageData();
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        if (pageIndex != null) {
            PageModel pageModel = new PageModel();
            pageModel.setPageIndex(pageIndex);
            if (pageSize == null) {
                pageModel.setPageSize(15);
            } else {
                pageModel.setPageSize(pageSize);
            }
            pageModel.setRecordCount(orderInfoMapper.findInfoCount(map));
            map.put("pageModel", pageModel);
            List<OrderInfo> orderInfos = orderInfoMapper.findINFOS(map);
            logger.info(map.toString());
            for (OrderInfo orderInfo : orderInfos) {
                map.clear();
                map.put("orderId", orderInfo.getOrderId());
                listModels.add(new OrderInfoViewListModel(orderInfo, orderItemMapper.findItems(map)));
            }
            pageData.setPageModel(pageModel);
            pageData.setModelData(listModels);
        } else {
            List<OrderInfo> orderInfos = orderInfoMapper.findINFOS(map);
            logger.info(map.toString());
            for (OrderInfo orderInfo : orderInfos) {
                map.clear();
                map.put("orderId", orderInfo.getOrderId());
                listModels.add(new OrderInfoViewListModel(orderInfo, orderItemMapper.findItems(map)));
            }
            pageData.setModelData(listModels);
        }
        return pageData;
    }

    @Override
    public PageData findGroupOrders(Map<String, Object> map) {
        List<GroupInfoModel> groupInfoModels = new ArrayList<>();
        PageData pageData = new PageData();
        Integer pageIndex = (Integer) map.get("pageIndex");
        Integer pageSize = (Integer) map.get("pageSize");
        List<OrderInfo> orderInfos;
        if (pageIndex != null) {
            PageModel pageModel = new PageModel();
            pageModel.setPageIndex(pageIndex);
            if (pageSize == null) {
                pageModel.setPageSize(15);
            } else {
                pageModel.setPageSize(pageSize);
            }
            pageModel.setRecordCount(orderInfoMapper.findGroupOrderCount(map));
            map.put("pageModel", pageModel);
            pageData.setPageModel(pageModel);
            orderInfos = orderInfoMapper.findGroupOrders(map);
        } else {
            orderInfos = orderInfoMapper.findGroupOrders(map);
        }
        for (OrderInfo orderInfo : orderInfos) {
            GroupInfoModel groupInfoModel = new GroupInfoModel();
            groupInfoModel.setOrderInfo(orderInfo);
            if (orderInfo.getGroupId() == null) {
                continue;
            }
            GroupInfo groupInfo = groupInfoBaseDao.findOne(orderInfo.getGroupId());
            groupInfoModel.setGroupInfo(groupInfo);
            groupInfoModel.setGroupBuy(groupBuyBaseDao.findOne(groupInfo.getBuyId()));
            OrderItem orderItem = orderItemMapper.findByOrderId(orderInfo.getOrderId());
            if (orderItem == null) {
                continue;
            }
            groupInfoModel.setOrderItem(orderItem);
            groupInfoModel.setCommoditySpecification(commoditySpecificationMapper.selectByPrimaryKey(orderItem.getSpecificationId()));
            groupInfoModel.setOrderInfo(orderInfo);
            groupInfoModels.add(groupInfoModel);
        }
        pageData.setModelData(groupInfoModels);
        return pageData;
    }

    @Override
    public OrderInfoViewModel findInfoModel(String orderId) {
        if (orderId == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>(1);

        OrderInfoViewModel orderInfoModel = new OrderInfoViewModel();
        orderInfoModel.setOrderInfo(orderInfoMapper.selectByPrimaryKey(orderId));
        map.put("orderId", orderInfoModel.getOrderInfo().getOrderId());
        orderInfoModel.setOrderItemModels(orderItemMapper.findItems(map));
        return orderInfoModel;
    }

    @Override
    public ResultData wxPay(String orderId, String ip) {
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
        PersonInfo personInfo = personInfoBaseDao.findOne(orderInfo.getPersonId());
        String orderPrice = String.valueOf(Math.round(orderInfo.getPayPrice() * 100));
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        if (orderInfo.getOrderCategory() == ORDER_GROUP_JOIN) {
            GroupInfo groupInfo = groupInfoBaseDao.findOne(orderInfo.getGroupId());
            GroupBuy groupBuy = groupBuyBaseDao.findOne(groupInfo.getBuyId());
            //是否可以参团
            if (!TimeUtil.comparingDate(dateTime, groupInfo.getEndTime()) || !(groupInfo.getGroupNumber() < groupBuy.getGroupNumber())) {
                orderInfo.setUpdateTime(dateTime);
                orderInfo.setOrderType(ORDER_NOT_ALLOW);
                orderInfo.setIsDelete(1);
                orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                return ResultData.returnResultData(-1, "团购已经关闭");
            }
        }
        Map<String, String> result = WXpaySDKUtil.payRequestSmallProgram("先小生订单", orderPrice, personInfo.getOpenid(), ip, orderInfo.getOrderId());
        if (result == null) {
            return ResultData.returnResultData(-1);
        }
        orderInfo.setPrepayId(result.get("prepay_id"));
        orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
        return ResultData.returnResultData(1, result);
    }

    @Override
    public int userDeletes(Operate operate) {
        if (operate == null) {
            return -1;
        }
        List orderIds = operate.getIds();
        if (orderIds == null || orderIds.size() < 1) {
            return -1;
        }
        return orderInfoMapper.deleteByIds(orderIds);
    }

    @Override
    public int payManual(Map<String, Object> map) {
        Double allowPrice = (Double) map.get("allowPrice");
        String orderId = (String) map.get("allowPrice");
        String adminName = (String) map.get("allowPrice");
        String adminPassword = (String) map.get("allowPrice");
        boolean bad = adminName == null || adminPassword == null || allowPrice == null || orderId == null;
        if (bad) {
            return -1;
        }
        if (adminInfoBaseDao.findByNameAndPassword(adminName, BaseMD5Tools.getMD5(adminPassword)) != null) {
            return orderInfoMapper.updatePayById(allowPrice, TimeUtil.getDateTime(1), orderId);
        }
        return 0;
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    @Override
    public String wxPayBack(String xmlString) {
        Map<String, String> map = null;
        try {
            map = WXPayUtil.xmlToMap(xmlString);
        } catch (Exception e) {
            logger.error(e.toString());
            return WXpaySDKUtil.setXML("FAIL", "解析出错");
        }
        String returnCode = map.get("return_code");
        logger.info("微信通知回执信息" + map.toString());
        //返回成功
        if (returnCode != null && returnCode.equals(SUCCESS)) {
            String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
            //签名成功
            if (WXpaySDKUtil.isPayResultNotifySignatureValid(map)) {
                String orderId = map.get("out_trade_no");
                OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
                Integer orderCategory = orderInfo.getOrderCategory();
                //团购订单
                if (orderCategory == ORDER_GROUP_OPEN || orderCategory == ORDER_GROUP_JOIN) {
                    logger.info("订单信息:" + orderInfo.toString());
                    GroupInfo groupInfo = groupInfoBaseDao.findOne(orderInfo.getGroupId());
                    groupInfo.setGroupNumber(groupInfo.getGroupNumber() + 1);
                    if (orderCategory == ORDER_GROUP_OPEN) {
                        //修改团购信息
                        groupInfo.setGroupType(GROUP_INFO_START);
                        groupInfo.setStartTime(dateTime);
                        // TODO
                        Date endDate = TimeUtil.getDateAddMinute(dateTime, 10);
                        groupInfo.setEndTime(TimeUtil.formatDate(TimeUtil.FormatType.TO_SEC, endDate));
                        //只使用一次且基于时间
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                //关闭团购
                                closeGroup(groupInfo.getId());
                            }
                        }, endDate);
                    }
                    PersonInfo personInfo = personInfoBaseDao.findOne(orderInfo.getPersonId());
                    GroupItem groupItem = new GroupItem();
                    groupItem.setCreateTime(dateTime);
                    groupItem.setGroupId(groupInfo.getId());
                    groupItem.setPersonId(personInfo.getId());
                    groupItem.setNickName(personInfo.getWxNico());
                    groupItem.setIconPath(personInfo.getWxIcon());
                    groupItem.setOrderId(orderId);
                    groupItem.setItemType(GROUP_ITEM_IN);
                    groupItem.setIsDelete(COMMON_NOT);
                    groupItemBaseDao.save(groupItem);
                    logger.info(groupInfoBaseDao.save(groupInfo).toString());
                    // 完成团购
                    if (groupInfo.getGroupNumber() >= groupBuyBaseDao.findOne(groupInfo.getBuyId()).getGroupNumber()) {
                        orderInfo.setOrderType(ORDER_PAY);
                        orderInfo.setAllowPrice(Double.valueOf(map.get("total_fee")) / 100);
                        groupInfo.setGroupType(GROUP_INFO_COMPLETE);
                        String settlementTotalFee = map.get("cash_fee");
                        groupItemDao.updateTypeByGroupId(groupInfo.getId(), dateTime, GROUP_ITEM_COMPLETE);
                        if (settlementTotalFee != null && !"".equals(settlementTotalFee)) {
                            orderInfo.setPayPrice(Double.valueOf(settlementTotalFee) / 100);
                        } else {
                            orderInfo.setPayPrice(orderInfo.getAllowPrice());
                        }
                        orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                        // TODO 发通知
                        Map<String, Object> map1 = new HashMap<>(3);
                        map1.put("isDelete", 0);
                        map1.put("groupId", groupInfo.getId());
                        List<OrderInfo> orderInfos = orderInfoMapper.findINFOS(map1);
                        sendWxMessage(orderInfos, dateTime, groupBuyBaseDao.findOne(groupInfo.getBuyId()));
                        orderInfoMapper.updateTypeByGroupId(ORDER_PAY, dateTime, groupInfo.getId());
                        return WXpaySDKUtil.setXML(SUCCESS, SUCCESS);
                    }
                    // 等待团购
                    else {
                        orderInfo.setOrderType(ORDER_GROUP_IN);
                    }
                    // 支付完成
                } else {
                    orderInfo.setOrderType(ORDER_PAY);
                }
                orderInfo.setAllowPrice(Double.valueOf(map.get("total_fee")) / 100);
                String settlementTotalFee = map.get("cash_fee");
                if (settlementTotalFee != null && !"".equals(settlementTotalFee)) {
                    orderInfo.setPayPrice(Double.valueOf(settlementTotalFee) / 100);
                } else {
                    orderInfo.setPayPrice(orderInfo.getAllowPrice());
                }
                orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                return WXpaySDKUtil.setXML(SUCCESS, SUCCESS);
            } else {
                logger.error("签名出错");
                return WXpaySDKUtil.setXML("FAIL", "签名错误");
            }
        } else {
            String returnMsg = map.get("return_msg");
            logger.error(returnMsg);
            return WXpaySDKUtil.setXML("FAIL", returnMsg);
        }

    }

    private void sendWxMessage(List<OrderInfo> orderInfos, String dateTime, GroupBuy groupBuy) {
        try {
            String accessToken = WXLoginConfig.getAccessToken(wxLoginConfig);
            if (accessToken == null) {
                return;
            }
            String url = wxLoginConfig.getMessagePath(accessToken);
            WxMessage clone = new WxMessage();
            for (OrderInfo orderInfo : orderInfos) {
                WxMessage wxMessage = clone.clone();
                List<KeyWord> keyWords = new ArrayList<>(10);
                keyWords.add(new KeyWord(dateTime, "#173177"));
                keyWords.add(new KeyWord("团购", "#173177"));
                keyWords.add(new KeyWord(String.valueOf(orderInfo.getAllowPrice()), "#173177"));
                keyWords.add(new KeyWord(groupBuy.getGroupName(), "#173177"));
                keyWords.add(new KeyWord("先小生拼团", "#173177"));
                keyWords.add(new KeyWord(String.valueOf(orderInfo.getPayPrice()), "#173177"));
                keyWords.add(new KeyWord("1", "#173177"));
                keyWords.add(new KeyWord("达到人数,拼团成功", "#173177"));
                wxMessage.setKeyWords(keyWords);
                wxMessage.setFromId(orderInfo.getPrepayId());
                wxMessage.setToUser(orderInfo.getOrderId());
                wxMessage.setTemplateId(ORDER_MESSAGE_ID);
                wxMessage.setPage("/order/order?id=" + orderInfo.getOrderId());
                WxMessageUtil.sendWxMessage(url, wxMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = Exception.class)
    @Override
    public String wxRefundBack(String xmlString) {
        Map<String, String> map;
        try {
            map = WXPayUtil.xmlToMap(xmlString);
        } catch (Exception e) {
            logger.error(e.toString());
            return WXpaySDKUtil.setXML("FAIL", "解析出错");
        }
        logger.info(map.toString());
        return WXpaySDKUtil.takeWxNotifyBack(map, new AbstractWxPayDo() {
            @Override
            public String run() {
                return decryptionRefundAndChange(map);
            }
        });

    }


    private String decryptionRefundAndChange(Map<String, String> map){
        String result = WXpaySDKUtil.decryptData(map.get("req_info"));
        try {
            Map<String, String> data = WXPayUtil.xmlToMap(result);
            String orderId = data.get("out_trade_no");
            OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(orderId);
            orderInfo.setOrderType(ORDER_COM_REFUSE);
            orderInfo.setUpdateTime(TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC));
        } catch (Exception e) {
            e.printStackTrace();
            return WXpaySDKUtil.setXML("FAIL", "解密失败");
        }
        return WXpaySDKUtil.setXML(SUCCESS, SUCCESS);
    }

    // 关闭团购 完成逻辑
    private void closeGroup(String groupId) {
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        GroupInfo groupInfo = groupInfoBaseDao.findOne(groupId);
        GroupBuy groupBuy = groupBuyBaseDao.findOne(groupInfo.getBuyId());
        if (groupInfo.getGroupNumber() < groupBuy.getGroupNumber()) {
            //修改信息
            groupInfo.setGroupType(GROUP_INFO_DED);
            groupInfo.setUpdateTime(dateTime);
            groupInfoBaseDao.save(groupInfo);
            groupItemDao.updateTypeByGroupId(groupId, dateTime, GROUP_ITEM_NOT);
            //退款
            Map<String, Object> map = new HashMap<>(2);
            map.put("isDelete", 0);
            map.put("groupId", groupId);
            List<OrderInfo> orderInfos = orderInfoMapper.findINFOS(map);
            for (OrderInfo orderInfo : orderInfos) {
                if (WXpaySDKUtil.refundOrder(orderInfo)) {
                    orderInfo.setOrderType(ORDER_IN_REFUSE);
                    orderInfo.setUpdateTime(dateTime);
                    orderInfoMapper.updateByPrimaryKeySelective(orderInfo);
                }
            }
        }
    }

    @Override
    public ResultData cancel(FindModel findModel) {
        String orderId = findModel.getOrderId();
        if (orderId == null || "".equals(orderId)) {
            return ResultData.returnResultData(-1);
        }
        closeOrderInfo(orderId);
        return ResultData.returnResultData(1);
    }

    @Override
    public ResultData send(FindModel findModel) {
        String orderId = findModel.getOrderId();
        if (orderId == null || "".equals(orderId)) {
            return ResultData.returnResultData(-1);
        }
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        return ResultData.returnResultData(orderInfoMapper.updateTypeById(orderId, dateTime, ORDER_IN_SEND));
    }

    @Override
    public ResultData receive(FindModel findModel) {
        String orderId = findModel.getOrderId();
        if (orderId == null || "".equals(orderId)) {
            return ResultData.returnResultData(-1);
        }
        // TODO 更新积分
        OrderInfo orderInfo = orderInfoMapper.selectByPrimaryKey(findModel.getOrderId());
        if (orderInfo == null) {
            return ResultData.returnResultData(-1);
        }
        PersonInfo personInfo = personInfoBaseDao.findOne(orderInfo.getPersonId());
        String dateTime = TimeUtil.getDateTime(TimeUtil.FormatType.TO_SEC);
        updatePersonVipGrade(orderInfo, personInfo);
        personInfoBaseDao.save(personInfo);
        return ResultData.returnResultData(orderInfoMapper.updateTypeById(orderId, dateTime, ORDER_COMPLETE));
    }

}
