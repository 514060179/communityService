package com.newland.property.dto.repair;

import com.newland.property.dto.PageDto;
import com.newland.property.dto.repair.RepairDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 报修回访数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class RepairReturnVisitDto extends RepairDto implements Serializable {

    private String visitId;
    private String context;
    private String repairId;
    private String communityId;
    private String visitPersonName;
    private String visitPersonId;
    private String visitType;

    private String state;

    private Date createTime;

    private String statusCd = "0";


    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    @Override
    public String getContext() {
        return context;
    }

    @Override
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String getRepairId() {
        return repairId;
    }

    @Override
    public void setRepairId(String repairId) {
        this.repairId = repairId;
    }

    @Override
    public String getCommunityId() {
        return communityId;
    }

    @Override
    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getVisitPersonName() {
        return visitPersonName;
    }

    public void setVisitPersonName(String visitPersonName) {
        this.visitPersonName = visitPersonName;
    }

    public String getVisitPersonId() {
        return visitPersonId;
    }

    public void setVisitPersonId(String visitPersonId) {
        this.visitPersonId = visitPersonId;
    }

    @Override
    public String getVisitType() {
        return visitType;
    }

    @Override
    public void setVisitType(String visitType) {
        this.visitType = visitType;
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
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }
}
