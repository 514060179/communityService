package com.newland.property.common.bmo.logSystemError;
import com.newland.property.po.log.LogSystemErrorPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteLogSystemErrorBMO {


    /**
     * 修改系统异常
     * add by wuxw
     * @param logSystemErrorPo
     * @return
     */
    ResponseEntity<String> delete(LogSystemErrorPo logSystemErrorPo);


}
