package com.newland.property.acct.payment;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.dto.payment.NotifyPaymentOrderDto;
import com.newland.property.dto.payment.PaymentOrderDto;

import java.util.Map;

public interface IPaymentFactoryAdapt {
    /**
     * 支付
     *
     * @param paymentOrderDto
     * @return
     */
    Map newlandPropertyPayment(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception;

    /**
     * 支付完成通知
     *
     * @param notifyPaymentOrderDto
     * @return
     */
    PaymentOrderDto newlandPropertyNotifyPayment(NotifyPaymentOrderDto notifyPaymentOrderDto);

    /**
     * 查询支付状态
     *
     * @param paymentOrderDto
     * @return
     */
    Map queryPaymentStatus(PaymentOrderDto paymentOrderDto, JSONObject reqJson, ICmdDataFlowContext context) throws Exception;
}
