package com.newland.property.job.bmo.businessDatabus;

import com.newland.property.dto.system.BusinessDatabusDto;
import org.springframework.http.ResponseEntity;

public interface IGetBusinessDatabusBMO {


    /**
     * 查询业务数据同步
     * add by wuxw
     *
     * @param businessDatabusDto
     * @return
     */
    ResponseEntity<String> get(BusinessDatabusDto businessDatabusDto);


}
