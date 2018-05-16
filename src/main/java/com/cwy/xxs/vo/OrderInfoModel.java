package com.cwy.xxs.vo;

/**
 * @author acy 屋大维
 */
public class OrderInfoModel {

    private Integer orderId;

    private String orderTag;

    private Double mailPrice;

    private String orderRemark;

    private Integer logisticsId;

    private Double payPrice;

    private String createTime;

    private String payTime;

    private Integer orderType;

    private String wxAddressInfo;

    private Integer storeId;

    private Integer companyId;

    private String companyName;

    private String companyCode;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderTag() {
        return orderTag;
    }

    public void setOrderTag(String orderTag) {
        this.orderTag = orderTag;
    }

    public Double getMailPrice() {
        return mailPrice;
    }

    public void setMailPrice(Double mailPrice) {
        this.mailPrice = mailPrice;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getWxAddressInfo() {
        return wxAddressInfo;
    }

    public void setWxAddressInfo(String wxAddressInfo) {
        this.wxAddressInfo = wxAddressInfo;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public Integer getLogisticsId() {
        return logisticsId;
    }

    public void setLogisticsId(Integer logisticsId) {
        this.logisticsId = logisticsId;
    }

    @Override
    public String toString() {
        return "OrderInfoModel{" +
                "orderId=" + orderId +
                ", orderTag='" + orderTag + '\'' +
                ", mailPrice=" + mailPrice +
                ", orderRemark='" + orderRemark + '\'' +
                ", logisticsId=" + logisticsId +
                ", payPrice=" + payPrice +
                ", createTime='" + createTime + '\'' +
                ", payTime='" + payTime + '\'' +
                ", orderType=" + orderType +
                ", wxAddressInfo='" + wxAddressInfo + '\'' +
                ", storeId=" + storeId +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
