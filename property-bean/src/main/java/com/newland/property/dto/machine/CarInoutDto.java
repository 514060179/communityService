package com.newland.property.dto.machine;

import com.newland.property.dto.payment.CarInoutPaymentDto;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 进出场数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class CarInoutDto extends CarInoutPaymentDto implements Serializable {

    //状态，100300 进场状态 100400 支付完成 100500 离场状态 100600 支付超时重新支付
    public static final String STATE_IN = "100300";
    public static final String STATE_PAY = "100400";
    public static final String STATE_OUT = "100500";
    public static final String STATE_REPAY = "100600";

    public static final String STATE_IN_FAIL = "100301";
    public static final String CAR_TYPE_MONTH = "1001";
    public static final String CAR_TYPE_TEMP = "1003";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    private String inTime;
    private String inoutId;
    private String carNum;
    private String oldCarNum;
    private String state;
    private String stateName;
    private String[] states;
    private String communityId;
    private String outTime;
    private String paId;
    private String[] paIds;
    private long hours;
    private long min;

    private String carType;
    private String carTypeName;


    private Date createTime;

    private String statusCd = "0";

    private String areaNum;
    private String feeName;
    private String feeConfigId;
    private String configId;

    private String startTime;
    private String endTime;


    @Override
    public String getInoutId() {
        return inoutId;
    }

    @Override
    public void setInoutId(String inoutId) {
        this.inoutId = inoutId;
    }

    @Override
    public String getCarNum() {
        return carNum;
    }

    @Override
    public void setCarNum(String carNum) {
        this.carNum = carNum;
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

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    @Override
    public String getInTime() {
        return inTime;
    }

    @Override
    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    @Override
    public String getStateName() {
        return stateName;
    }

    @Override
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String getPaId() {
        return paId;
    }

    @Override
    public void setPaId(String paId) {
        this.paId = paId;
    }

    public long getHours() {
        try {
            if (outTime != null && inTime != null) {
                long min = (sdf.parse(outTime).getTime() - sdf.parse(inTime).getTime()) / (60 * 1000);
                return min / 60;
            }else if (inTime != null && outTime == null) {
                long min = (new Date().getTime() - sdf.parse(inTime).getTime()) / (60 * 1000);
                return min / 60;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return hours;
    }

    public void setHours(long hours) {
        this.hours = hours;
    }

    public long getMin() {
        try {
            if (outTime != null && inTime != null) {
                long min = (sdf.parse(outTime).getTime() - sdf.parse(inTime).getTime()) / (60 * 1000);
                return min % 60;
            }else if (inTime != null && outTime == null) {
                long min = (new Date().getTime() - sdf.parse(inTime).getTime()) / (60 * 1000);
                return min % 60;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return min;
    }

    public void setMin(long min) {
        this.min = min;
    }

    @Override
    public String[] getPaIds() {
        return paIds;
    }

    @Override
    public void setPaIds(String[] paIds) {
        this.paIds = paIds;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getAreaNum() {
        return areaNum;
    }

    public void setAreaNum(String areaNum) {
        this.areaNum = areaNum;
    }

    public String getFeeName() {
        return feeName;
    }

    public void setFeeName(String feeName) {
        this.feeName = feeName;
    }

    public String getFeeConfigId() {
        return feeConfigId;
    }

    public void setFeeConfigId(String feeConfigId) {
        this.feeConfigId = feeConfigId;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
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

    public String getOldCarNum() {
        return oldCarNum;
    }

    public void setOldCarNum(String oldCarNum) {
        this.oldCarNum = oldCarNum;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }
}
