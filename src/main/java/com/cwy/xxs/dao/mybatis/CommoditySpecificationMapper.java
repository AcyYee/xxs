package com.cwy.xxs.dao.mybatis;

import com.cwy.xxs.entity.CommoditySpecification;
import com.cwy.xxs.vo.SpecificationCountModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CommoditySpecificationMapper {
    int deleteByPrimaryKey(Integer specificationId);

    int insert(CommoditySpecification record);

    int insertSelective(CommoditySpecification record);

    CommoditySpecification selectByPrimaryKey(Integer specificationId);

    int updateByPrimaryKeySelective(CommoditySpecification record);

    int updateByPrimaryKey(CommoditySpecification record);

    int selectSpecificationCount(Map<String, Object> map);

    List<CommoditySpecification> selectSpecifications(Map<String, Object> map);

    int updateSortNumber(@Param("specificationId") Integer specificationId, @Param("sortNumber") Integer sortNumber);

    void updateSortNumbers(Integer sortNumber);

    int deleteByIds(@Param("specificationIds") List specificationIds);

}