package com.newland.property.store.bmo.contract;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.po.contract.ContractPo;
import org.springframework.http.ResponseEntity;

public interface ISaveContractBMO {


    /**
     * 添加合同管理
     * add by wuxw
     *
     * @param contractPo
     * @return
     */
    ResponseEntity<String> save(ContractPo contractPo, JSONObject reqJson);


}
