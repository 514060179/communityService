package com.newland.property.dto.payFeeRuleBill;

import com.newland.property.dto.PageDto;
import com.newland.property.dto.fee.FeeDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 费用账单数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class PayFeeRuleBillDto extends FeeDto implements Serializable {

    private String billName;
    private String configId;
    private String billId;
    private String curYearMonth;
    private String remark;
    private String ruleId;
    private String communityId;
    private String batchId;
    private String feeId;

    private String monthCycle;


    private Date createTime;

    private String statusCd = "0";


    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    @Override
    public String getConfigId() {
        return configId;
    }

    @Override
    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCurYearMonth() {
        return curYearMonth;
    }

    public void setCurYearMonth(String curYearMonth) {
        this.curYearMonth = curYearMonth;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getRuleId() {
        return ruleId;
    }

    @Override
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
    public String getBatchId() {
        return batchId;
    }

    @Override
    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    @Override
    public String getFeeId() {
        return feeId;
    }

    @Override
    public void setFeeId(String feeId) {
        this.feeId = feeId;
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

    @Override
    public String getMonthCycle() {
        return monthCycle;
    }

    @Override
    public void setMonthCycle(String monthCycle) {
        this.monthCycle = monthCycle;
    }
}
