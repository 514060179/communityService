package com.newland.property.vo.api.bocpay;

public class BocChannelRequest {

    private String requestId;

    private String service;

    private String version;

    private String ipAddress;

    private String signType;

    private String merchantId;

    private String terminalNo;

    private String merchantSign;

    public BocChannelRequest(String service, String merchantId, String terminalNo) {
        this.requestId = "";
        this.service = service;
        this.merchantId = merchantId;
        this.terminalNo = terminalNo;
        this.signType = "RSA2";
        this.version = "2.0";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getTerminalNo() {
        return terminalNo;
    }

    public void setTerminalNo(String terminalNo) {
        this.terminalNo = terminalNo;
    }

    public String getMerchantSign() {
        return merchantSign;
    }

    public void setMerchantSign(String merchantSign) {
        this.merchantSign = merchantSign;
    }
}
