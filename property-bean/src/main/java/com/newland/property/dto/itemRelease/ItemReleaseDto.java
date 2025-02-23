package com.newland.property.dto.itemRelease;

import com.newland.property.dto.PageDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 放行管理数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ItemReleaseDto extends ItemReleaseTypeDto implements Serializable {

    //状态 W待审核 D 审核中 C 审核完成 D 审核失败
    //W待审核
    public static final String STATE_WAIT = "W";
    //审核中
    public static final String STATE_DOING = "D";
    //审核完成
    public static final String STATE_COMPLETE = "C";
    //审核失败
    public static final String STATE_FAIT = "F";


    private String amount;
    private String applyCompany;
    private String idCard;
    private String passTime;
    private String carNum;
    private String remark;
    private String applyPerson;
    private String irId;
    private String[] irIds;
    private String applyTel;
    private String typeId;
    private String state;
    private String communityId;

    private String stateName;


    private Date createTime;

    private String createUserId;

    private String statusCd = "0";


    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getApplyCompany() {
        return applyCompany;
    }

    public void setApplyCompany(String applyCompany) {
        this.applyCompany = applyCompany;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPassTime() {
        return passTime;
    }

    public void setPassTime(String passTime) {
        this.passTime = passTime;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getApplyPerson() {
        return applyPerson;
    }

    public void setApplyPerson(String applyPerson) {
        this.applyPerson = applyPerson;
    }

    public String getIrId() {
        return irId;
    }

    public void setIrId(String irId) {
        this.irId = irId;
    }

    public String getApplyTel() {
        return applyTel;
    }

    public void setApplyTel(String applyTel) {
        this.applyTel = applyTel;
    }

    @Override
    public String getTypeId() {
        return typeId;
    }

    @Override
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
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

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String[] getIrIds() {
        return irIds;
    }

    public void setIrIds(String[] irIds) {
        this.irIds = irIds;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }
}
