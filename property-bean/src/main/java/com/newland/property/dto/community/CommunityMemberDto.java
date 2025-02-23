package com.newland.property.dto.community;

import com.newland.property.dto.community.CommunityDto;

import java.io.Serializable;

/**
 * 小区成员dto
 */
public class CommunityMemberDto extends CommunityDto implements Serializable {

    // 审核通过
    public static final String AUDIT_STATUS_NORMAL = "1100";

    public static final String MEMBER_TYPE_PROPERTY = "390001200002";

    private String communityMemberId;

    private String communityId;
    private String communityName;

    private String memberId;
    private String subMemberId;

    private String memberTypeCd;

    private String auditStatusCd;

    private String[] auditStatusCds;

    private String statusCd = "0";

    private boolean needCommunityInfo;

    private boolean noAuditEnterCommunity;

    private String startTime;
    private String endTime;

    public String getCommunityMemberId() {
        return communityMemberId;
    }

    public void setCommunityMemberId(String communityMemberId) {
        this.communityMemberId = communityMemberId;
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
    public String getMemberId() {
        return memberId;
    }

    @Override
    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberTypeCd() {
        return memberTypeCd;
    }

    public void setMemberTypeCd(String memberTypeCd) {
        this.memberTypeCd = memberTypeCd;
    }

    @Override
    public String getAuditStatusCd() {
        return auditStatusCd;
    }

    @Override
    public void setAuditStatusCd(String auditStatusCd) {
        this.auditStatusCd = auditStatusCd;
    }

    @Override
    public String getStatusCd() {
        return statusCd;
    }

    @Override
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public boolean isNeedCommunityInfo() {
        return needCommunityInfo;
    }

    public void setNeedCommunityInfo(boolean needCommunityInfo) {
        this.needCommunityInfo = needCommunityInfo;
    }

    public boolean isNoAuditEnterCommunity() {
        return noAuditEnterCommunity;
    }

    public void setNoAuditEnterCommunity(boolean noAuditEnterCommunity) {
        this.noAuditEnterCommunity = noAuditEnterCommunity;
    }

    public String[] getAuditStatusCds() {
        return auditStatusCds;
    }

    public void setAuditStatusCds(String[] auditStatusCds) {
        this.auditStatusCds = auditStatusCds;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getSubMemberId() {
        return subMemberId;
    }

    public void setSubMemberId(String subMemberId) {
        this.subMemberId = subMemberId;
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
}
