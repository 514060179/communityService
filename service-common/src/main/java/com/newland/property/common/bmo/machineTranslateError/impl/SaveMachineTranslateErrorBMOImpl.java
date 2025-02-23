package com.newland.property.common.bmo.machineTranslateError.impl;

import com.newland.property.common.bmo.machineTranslateError.ISaveMachineTranslateErrorBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.newland.property.po.machine.MachineTranslateErrorPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveMachineTranslateErrorBMOImpl")
public class SaveMachineTranslateErrorBMOImpl implements ISaveMachineTranslateErrorBMO {

    @Autowired
    private IMachineTranslateErrorInnerServiceSMO machineTranslateErrorInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param machineTranslateErrorPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(MachineTranslateErrorPo machineTranslateErrorPo) {

        machineTranslateErrorPo.setLogId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_logId));
        int flag = machineTranslateErrorInnerServiceSMOImpl.saveMachineTranslateError(machineTranslateErrorPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
