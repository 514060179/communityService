package com.newland.property.vo.api.bocpay;

public class BocChannelQueryRequestBody extends BocChannelRequest {

    private String queryNo;

    private String queryLogNo;

    public String getQueryNo() {
        return queryNo;
    }

    public void setQueryNo(String queryNo) {
        this.queryNo = queryNo;
    }

    public String getQueryLogNo() {
        return queryLogNo;
    }

    public void setQueryLogNo(String queryLogNo) {
        this.queryLogNo = queryLogNo;
    }

    public BocChannelQueryRequestBody(String merchantId, String terminalNo) {
        super("OrderQuery", merchantId, terminalNo);
    }

}