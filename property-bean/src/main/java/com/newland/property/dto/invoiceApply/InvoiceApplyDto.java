package com.newland.property.dto.invoiceApply;

import com.newland.property.dto.PageDto;
import com.newland.property.dto.ownerInvoice.OwnerInvoiceDto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName FloorDto
 * @Description 发票申请数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class InvoiceApplyDto extends OwnerInvoiceDto implements Serializable {

    //W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    public static final String STATE_WAIT = "W";
    //W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    public static final String STATE_UPLOAD = "U";
    //W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    public static final String STATE_FAIL = "F";
    //W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    public static final String STATE_GET = "G";
    //W待审核 U 待上传 F 审核失败 G 带领用 C 已领用
    public static final String STATE_COMPLETE = "C";


    private String applyId;
    private String oiId;
    private String createUserId;
    private String ownerName;
    private String invoiceType;
    private String applyTel;
    private String invoiceAmount;
    private String createUserName;
    private String remark;
    private String state;
    private String stateName;
    private String communityId;

    private String invoiceCode;


    private Date createTime;

    private String statusCd = "0";

    private List<String> urls;


    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    @Override
    public String getOiId() {
        return oiId;
    }

    @Override
    public void setOiId(String oiId) {
        this.oiId = oiId;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String getInvoiceType() {
        return invoiceType;
    }

    @Override
    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getApplyTel() {
        return applyTel;
    }

    public void setApplyTel(String applyTel) {
        this.applyTel = applyTel;
    }

    public String getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(String invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getInvoiceCode() {
        return invoiceCode;
    }

    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
    }
}
