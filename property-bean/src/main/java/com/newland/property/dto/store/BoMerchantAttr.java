package com.newland.property.dto.store;


import com.newland.property.dto.DefaultBoAttrEntity;

import java.util.Date;

/**
 * 商户属性表 bo_merchant_type
 * Created by wuxw on 2017/5/20.
 */
public class BoMerchantAttr extends DefaultBoAttrEntity implements Comparable<BoMerchantAttr> {

    private String boId;

    private String merchantId;

    private String attrCd;

    private String value;

    private String state;

    private Date create_dt;


    @Override
    public String getBoId() {
        return boId;
    }

    @Override
    public void setBoId(String boId) {
        this.boId = boId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    @Override
    public String getAttrCd() {
        return attrCd;
    }

    @Override
    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
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
    public Date getCreate_dt() {
        return create_dt;
    }

    @Override
    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    /**
     * 将过程数据转为实例数据
     * @return
     */
    public MerchantAttr convert(){
        MerchantAttr merchantAttr = new MerchantAttr();

        merchantAttr.setMerchantId(this.getMerchantId());
        merchantAttr.setAttrCd(this.getAttrCd());
        merchantAttr.setValue(this.getValue());
        return merchantAttr;
    }

    @Override
    public int compareTo(BoMerchantAttr otherBoMerchant) {
        if("DEL".equals(this.getState()) && "ADD".equals(otherBoMerchant.getState())) {
            return -1;
        }
        return 0;
    }
}
