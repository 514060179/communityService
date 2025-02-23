package com.newland.property.dto.maintainance;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 保养标准数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class MaintainanceStandardItemDto extends MaintainanceItemDto implements Serializable {

    private String itemId;
    private String standardId;
    private String[] standardIds;
    private String msiId;
    private String communityId;

    private long itemCount;


    private Date createTime;

    private String statusCd = "0";


    @Override
    public String getItemId() {
        return itemId;
    }

    @Override
    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getStandardId() {
        return standardId;
    }

    @Override
    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public String getMsiId() {
        return msiId;
    }

    public void setMsiId(String msiId) {
        this.msiId = msiId;
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

    public String[] getStandardIds() {
        return standardIds;
    }

    public void setStandardIds(String[] standardIds) {
        this.standardIds = standardIds;
    }

    public long getItemCount() {
        return itemCount;
    }

    public void setItemCount(long itemCount) {
        this.itemCount = itemCount;
    }
}
