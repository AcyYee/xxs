package com.cwy.xxs.service;

import com.cwy.xxs.entity.OrderComment;
import com.cwy.xxs.vo.Operate;
import com.cwy.xxs.vo.ResultData;

import java.util.Map; /**
 * @author acy19
 */
public interface OrderCommentService {

    ResultData findComments(Map<String, Object> map);

    ResultData deleteComments(Operate operate);

    ResultData addComment(OrderComment orderComment);
}
