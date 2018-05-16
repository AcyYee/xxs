package com.cwy.xxs.entity;

/**
 * @author acy 屋大维
 */
public class StoreSpecification {

    private Integer specificationId;

    private String specificationName;

    private Integer specificationCount;

    private Double specificationPrice;

    private Integer isDelete;

    public Integer getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(Integer specificationId) {
        this.specificationId = specificationId;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName == null ? null : specificationName.trim();
    }

    public Integer getSpecificationCount() {
        return specificationCount;
    }

    public void setSpecificationCount(Integer specificationCount) {
        this.specificationCount = specificationCount;
    }

    public Double getSpecificationPrice() {
        return specificationPrice;
    }

    public void setSpecificationPrice(Double specificationPrice) {
        this.specificationPrice = specificationPrice;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

}