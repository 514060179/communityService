package com.newland.property.common.bmo.machineTranslateError;

import com.newland.property.po.machine.MachineTranslateErrorPo;
import org.springframework.http.ResponseEntity;

public interface ISaveMachineTranslateErrorBMO {


    /**
     * 添加IOT同步错误日志记录
     * add by wuxw
     *
     * @param machineTranslateErrorPo
     * @return
     */
    ResponseEntity<String> save(MachineTranslateErrorPo machineTranslateErrorPo);


}
