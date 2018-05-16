package com.cwy.xxs.vo;

import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class PageData {

    //分页信息
    private Pageable pageable;

    private PageModel pageModel;

    //当前数据
    private Object modelData;

}
