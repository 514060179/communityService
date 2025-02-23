package com.newland.property.fee.bmo;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.fee.FeeAttrDto;
import org.springframework.http.ResponseEntity;

public interface IQueryFeeByAttr {

    /**
     * 查询费用
     * @param feeAttrDto
     * @return
     */
    ResponseEntity<String> query(FeeAttrDto feeAttrDto);
}
