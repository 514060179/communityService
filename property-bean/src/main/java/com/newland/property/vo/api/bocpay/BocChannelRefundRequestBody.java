package com.newland.property.vo.api.bocpay;

public class BocChannelRefundRequestBody extends BocChannelRequest {

    private String payOrderNo;

    private String logNo;

    private String refundOrderNo;

    private String refundAmount;

    public BocChannelRefundRequestBody(String merchantId, String terminalNo) {
        super("OrderRefund", merchantId, terminalNo);
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getLogNo() {
        return logNo;
    }

    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    public String getRefundOrderNo() {
        return refundOrderNo;
    }

    public void setRefundOrderNo(String refundOrderNo) {
        this.refundOrderNo = refundOrderNo;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }
}
