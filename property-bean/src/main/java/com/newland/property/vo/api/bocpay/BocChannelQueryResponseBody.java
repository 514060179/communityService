package com.newland.property.vo.api.bocpay;

import com.alibaba.fastjson.JSON;

public class BocChannelQueryResponseBody extends BocChannelRespone {

    private String logNo;

    private String orderDate;

    private String orderTime;

    private String txnFlag;

    private String result;

    private String payChannel;

    private String amount;

    private String merchantOrderNo;

    private String fee;

    private String refundAmount;

    private String thirdLogNo;

    private String cashFee;

    private String cashFeeType;

    private String resultMessage;

    private String userId;

    private String merchantMarketFlag;

    private String marketCnName;

    private String merchantMarketId;

    private String marketEnName;

    private String actualPayAmount;

    private String reserved1;

    private String reserved2;

    private String reserved3;

    private String payDate;

    private String payTime;

    public String getLogNo() {
        return logNo;
    }

    public void setLogNo(String logNo) {
        this.logNo = logNo;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTxnFlag() {
        return txnFlag;
    }

    public void setTxnFlag(String txnFlag) {
        this.txnFlag = txnFlag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMerchantOrderNo() {
        return merchantOrderNo;
    }

    public void setMerchantOrderNo(String merchantOrderNo) {
        this.merchantOrderNo = merchantOrderNo;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getThirdLogNo() {
        return thirdLogNo;
    }

    public void setThirdLogNo(String thirdLogNo) {
        this.thirdLogNo = thirdLogNo;
    }

    public String getCashFee() {
        return cashFee;
    }

    public void setCashFee(String cashFee) {
        this.cashFee = cashFee;
    }

    public String getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(String cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchantMarketFlag() {
        return merchantMarketFlag;
    }

    public void setMerchantMarketFlag(String merchantMarketFlag) {
        this.merchantMarketFlag = merchantMarketFlag;
    }

    public String getMarketCnName() {
        return marketCnName;
    }

    public void setMarketCnName(String marketCnName) {
        this.marketCnName = marketCnName;
    }

    public String getMerchantMarketId() {
        return merchantMarketId;
    }

    public void setMerchantMarketId(String merchantMarketId) {
        this.merchantMarketId = merchantMarketId;
    }

    public String getMarketEnName() {
        return marketEnName;
    }

    public void setMarketEnName(String marketEnName) {
        this.marketEnName = marketEnName;
    }

    public String getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(String actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1;
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2;
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3;
    }

    public String getPayDate() {
        return payDate;
    }

    public void setPayDate(String payDate) {
        this.payDate = payDate;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}