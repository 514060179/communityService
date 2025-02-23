package com.newland.property.fee.bmo.payFeeDetailMonth.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.fee.bmo.payFeeDetailMonth.IUpdatePayFeeDetailMonthBMO;
import com.newland.property.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeDetailMonthPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updatePayFeeDetailMonthBMOImpl")
public class UpdatePayFeeDetailMonthBMOImpl implements IUpdatePayFeeDetailMonthBMO {

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;

    /**
     * @param payFeeDetailMonthPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(PayFeeDetailMonthPo payFeeDetailMonthPo) {

        int flag = payFeeDetailMonthInnerServiceSMOImpl.updatePayFeeDetailMonth(payFeeDetailMonthPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
