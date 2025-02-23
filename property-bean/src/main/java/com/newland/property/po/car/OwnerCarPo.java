package com.newland.property.po.car;

import java.io.Serializable;

/**
 * @ClassName OwnerCarPo
 * @Description TODO
 * @Author wuxw
 * @Date 2020/5/29 14:56
 * @Version 1.0
 * add by wuxw 2020/5/29
 **/
public class OwnerCarPo implements Serializable {


    private String carId;
    private String ownerId;
    private String carNum;
    private String carBrand;
    private String carType;
    private String carColor;
    private String psId;
    private String userId;
    private String remark;

    private String startTime;

    private String endTime;

    //状态，1001 正常状态，2002 车位释放欠费状态  3003 车位释放
    private String state;
    private String bId;

    private String communityId;
    private String carTypeCd;
    private String memberId;
    private String statusCd = "0";

    //'租赁类型，H 月租车 S 出售车 I 内部车 NM 免费车'
    private String leaseType;
    //收费金额
    private String balanceMoney;

    //支付方式 0 现金 1微信 2支付宝
    private Integer payType;

    //收费项ID
    private String configId;

    //停车场编号(外部编号num)
    private String areaNum;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getPsId() {
        return psId;
    }

    public void setPsId(String psId) {
        this.psId = psId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCarTypeCd() {
        return carTypeCd;
    }

    public void setCarTypeCd(String carTypeCd) {
        this.carTypeCd = carTypeCd;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }

    public String getbId() {
        return bId;
    }

    public void setbId(String bId) {
        this.bId = bId;
    }

    public String getLeaseType() {
        return leaseType;
    }

    public void setLeaseType(String leaseType) {
        this.leaseType = leaseType;
    }

    public String getBalanceMoney() {
        return balanceMoney;
    }

    public void setBalanceMoney(String balanceMoney) {
        this.balanceMoney = balanceMoney;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }
}
