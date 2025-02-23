package com.newland.property.dto.resource;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName FloorDto
 * @Description 物品次数数据层封装
 * @Author wuxw
 * @Date 2019/4/24 8:52
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
public class ResourceStoreTimesDto extends ResourceStoreDto implements Serializable {

    private String price;
    private String totalPrice;
    private String applyOrderId;
    private String storeId;
    private String stock;

    private String hasStock;
    private String resCode;
    private String resCodeLike;
    private String[] resCodes;
    private String timesId;
    private String shId;


    private Date createTime;

    private String statusCd = "0";

    private String communityId;


    @Override
    public String getPrice() {
        return price;
    }

    @Override
    public void setPrice(String price) {
        this.price = price;
    }

    public String getApplyOrderId() {
        return applyOrderId;
    }

    public void setApplyOrderId(String applyOrderId) {
        this.applyOrderId = applyOrderId;
    }

    @Override
    public String getStoreId() {
        return storeId;
    }

    @Override
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Override
    public String getStock() {
        return stock;
    }

    @Override
    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public String getResCode() {
        return resCode;
    }

    @Override
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    @Override
    public String getTimesId() {
        return timesId;
    }

    @Override
    public void setTimesId(String timesId) {
        this.timesId = timesId;
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

    public String getHasStock() {
        return hasStock;
    }

    public void setHasStock(String hasStock) {
        this.hasStock = hasStock;
    }

    @Override
    public String getTotalPrice() {
        return totalPrice;
    }

    @Override
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String[] getResCodes() {
        return resCodes;
    }

    @Override
    public void setResCodes(String[] resCodes) {
        this.resCodes = resCodes;
    }

    @Override
    public String getShId() {
        return shId;
    }

    @Override
    public void setShId(String shId) {
        this.shId = shId;
    }

    @Override
    public String getResCodeLike() {
        return resCodeLike;
    }

    @Override
    public void setResCodeLike(String resCodeLike) {
        this.resCodeLike = resCodeLike;
    }

    @Override
    public String getCommunityId() {
        return communityId;
    }

    @Override
    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
}
