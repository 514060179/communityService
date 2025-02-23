package com.newland.property.common.bmo.machineTranslateError;

import com.newland.property.po.machine.MachineTranslateErrorPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteMachineTranslateErrorBMO {


    /**
     * 修改IOT同步错误日志记录
     * add by wuxw
     *
     * @param machineTranslateErrorPo
     * @return
     */
    ResponseEntity<String> delete(MachineTranslateErrorPo machineTranslateErrorPo);


}
