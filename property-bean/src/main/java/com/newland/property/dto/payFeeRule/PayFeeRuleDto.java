package com.newland.property.dto.payFeeRule;

import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeConfigDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用规则数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeRuleDto extends FeeConfigDto implements Serializable {

    private String maxTime;

    private String feeTypeCd;
    private String curYearMonth;
    private String batchId;
    private String userId;
    private String incomeObjId;
    private String configId;
    private String feeFlag;
    private String startTime;
    private String endTime;
    private String state;
    private String ruleId;
    private String[] ruleIds;
    private String communityId;
    private String payerObjType;
    private String payerObjId;


    private Date createTime;

    private String statusCd = "0";


    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public String getFeeTypeCd() {
        return feeTypeCd;
    }

    @Override
    public void setFeeTypeCd(String feeTypeCd) {
        this.feeTypeCd = feeTypeCd;
    }

    public String getCurYearMonth() {
        return curYearMonth;
    }

    public void setCurYearMonth(String curYearMonth) {
        this.curYearMonth = curYearMonth;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIncomeObjId() {
        return incomeObjId;
    }

    public void setIncomeObjId(String incomeObjId) {
        this.incomeObjId = incomeObjId;
    }

    @Override
    public String getConfigId() {
        return configId;
    }

    @Override
    public void setConfigId(String configId) {
        this.configId = configId;
    }

    @Override
    public String getFeeFlag() {
        return feeFlag;
    }

    @Override
    public void setFeeFlag(String feeFlag) {
        this.feeFlag = feeFlag;
    }

    @Override
    public String getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String getEndTime() {
        return endTime;
    }

    @Override
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    @Override
    public String getCommunityId() {
        return communityId;
    }

    @Override
    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    @Override
    public String getPayerObjType() {
        return payerObjType;
    }

    @Override
    public void setPayerObjType(String payerObjType) {
        this.payerObjType = payerObjType;
    }

    public String getPayerObjId() {
        return payerObjId;
    }

    public void setPayerObjId(String payerObjId) {
        this.payerObjId = payerObjId;
    }


    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getStatusCd() {
        return statusCd;
    }

    @Override
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String[] getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(String[] ruleIds) {
        this.ruleIds = ruleIds;
    }
}
