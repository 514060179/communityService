package com.newland.property.common.bmo.machineTranslateError.impl;

import com.newland.property.common.bmo.machineTranslateError.IDeleteMachineTranslateErrorBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IMachineTranslateErrorInnerServiceSMO;
import com.newland.property.po.machine.MachineTranslateErrorPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteMachineTranslateErrorBMOImpl")
public class DeleteMachineTranslateErrorBMOImpl implements IDeleteMachineTranslateErrorBMO {

    @Autowired
    private IMachineTranslateErrorInnerServiceSMO machineTranslateErrorInnerServiceSMOImpl;

    /**
     * @param machineTranslateErrorPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(MachineTranslateErrorPo machineTranslateErrorPo) {

        int flag = machineTranslateErrorInnerServiceSMOImpl.deleteMachineTranslateError(machineTranslateErrorPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
