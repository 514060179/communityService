package com.newland.property.common.bmo.logSystemError.impl;

import com.newland.property.common.bmo.logSystemError.IUpdateLogSystemErrorBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.ILogSystemErrorInnerServiceSMO;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateLogSystemErrorBMOImpl")
public class UpdateLogSystemErrorBMOImpl implements IUpdateLogSystemErrorBMO {

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    /**
     * @param logSystemErrorPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(LogSystemErrorPo logSystemErrorPo) {

        int flag = logSystemErrorInnerServiceSMOImpl.updateLogSystemError(logSystemErrorPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
