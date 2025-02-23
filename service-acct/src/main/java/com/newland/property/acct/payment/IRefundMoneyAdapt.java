package com.newland.property.acct.payment;

import com.newland.property.dto.paymentPool.PaymentPoolDto;
import com.newland.property.dto.wechat.OnlinePayDto;
import com.newland.property.vo.ResultVo;

public interface IRefundMoneyAdapt {

    /**
     * 退款处理
     * @param onlinePayDto
     * @param paymentPoolDto
     * @return
     */
    ResultVo refund(OnlinePayDto onlinePayDto, PaymentPoolDto paymentPoolDto) throws Exception;
}
