package com.newland.property.job.bmo.businessDatabus;

import com.newland.property.po.business.BusinessDatabusPo;
import org.springframework.http.ResponseEntity;

public interface ISaveBusinessDatabusBMO {


    /**
     * 添加业务数据同步
     * add by wuxw
     *
     * @param businessDatabusPo
     * @return
     */
    ResponseEntity<String> save(BusinessDatabusPo businessDatabusPo);


}
