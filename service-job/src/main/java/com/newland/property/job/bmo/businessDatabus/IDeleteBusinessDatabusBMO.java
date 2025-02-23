package com.newland.property.job.bmo.businessDatabus;
import com.newland.property.po.business.BusinessDatabusPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteBusinessDatabusBMO {


    /**
     * 修改业务数据同步
     * add by wuxw
     * @param businessDatabusPo
     * @return
     */
    ResponseEntity<String> delete(BusinessDatabusPo businessDatabusPo);


}
