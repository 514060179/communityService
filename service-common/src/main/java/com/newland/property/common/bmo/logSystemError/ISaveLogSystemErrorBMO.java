package com.newland.property.common.bmo.logSystemError;

import com.newland.property.po.log.LogSystemErrorPo;
import org.springframework.http.ResponseEntity;
public interface ISaveLogSystemErrorBMO {


    /**
     * 添加系统异常
     * add by wuxw
     * @param logSystemErrorPo
     * @return
     */
    ResponseEntity<String> save(LogSystemErrorPo logSystemErrorPo);


}
