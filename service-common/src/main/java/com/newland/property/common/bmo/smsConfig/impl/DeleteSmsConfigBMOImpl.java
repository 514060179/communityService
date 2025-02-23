package com.newland.property.common.bmo.smsConfig.impl;

import com.newland.property.common.bmo.smsConfig.IDeleteSmsConfigBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.ISmsConfigInnerServiceSMO;
import com.newland.property.po.sms.SmsConfigPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteSmsConfigBMOImpl")
public class DeleteSmsConfigBMOImpl implements IDeleteSmsConfigBMO {

    @Autowired
    private ISmsConfigInnerServiceSMO smsConfigInnerServiceSMOImpl;

    /**
     * @param smsConfigPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(SmsConfigPo smsConfigPo) {

        int flag = smsConfigInnerServiceSMOImpl.deleteSmsConfig(smsConfigPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
