package com.newland.property.common.bmo.logSystemError.impl;

import com.newland.property.common.bmo.logSystemError.ISaveLogSystemErrorBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.ILogSystemErrorInnerServiceSMO;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveLogSystemErrorBMOImpl")
public class SaveLogSystemErrorBMOImpl implements ISaveLogSystemErrorBMO {

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param logSystemErrorPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(LogSystemErrorPo logSystemErrorPo) {

        logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
        int flag = logSystemErrorInnerServiceSMOImpl.saveLogSystemError(logSystemErrorPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
