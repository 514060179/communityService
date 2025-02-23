package com.newland.property.core.context;

import java.util.Date;

/**
 * @author wux
 * @create 2019-02-07 下午7:01
 * @desc 订单通知数据流信息
 **/
public abstract class AbstractOrderNotifyDataFlowContext extends AbstractDataFlowContextPlus implements IOrderNotifyDataFlowContext,IOrderResponse {
    protected AbstractOrderNotifyDataFlowContext(Date startDate, String code) {

    }


    private String transactionId;
    private Date responseTime;
    private String orderTypeCd;
    private String businessType;

    private String code;

    private String message;

    private String bId;

    private String businessTypeCd;

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String getOrderTypeCd() {
        return orderTypeCd;
    }

    public void setOrderTypeCd(String orderTypeCd) {
        this.orderTypeCd = orderTypeCd;
    }

    @Override
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Override
    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    @Override
    public String getBusinessTypeCd() {
        return businessTypeCd;
    }

    public void setBusinessTypeCd(String businessTypeCd) {
        this.businessTypeCd = businessTypeCd;
    }


}
