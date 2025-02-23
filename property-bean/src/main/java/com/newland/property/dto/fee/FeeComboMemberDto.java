package com.newland.property.dto.fee;

import com.newland.property.dto.fee.FeeConfigDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 套餐成员数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class FeeComboMemberDto extends FeeConfigDto implements Serializable {

    private String configId;
    private String comboId;
    private String communityId;
    private String memberId;
    private Date createTime;
    private String statusCd = "0";

    @Override
    public String getConfigId() {
        return configId;
    }

    @Override
    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }

    @Override
    public String getCommunityId() {
        return communityId;
    }

    @Override
    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
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
}
