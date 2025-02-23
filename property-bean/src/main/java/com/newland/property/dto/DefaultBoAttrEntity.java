package com.newland.property.dto;

/**
 * Created by wuxw on 2017/9/15.
 */
public class DefaultBoAttrEntity extends DefaultBoEntity {

    //属性编码
    private String attrCd;

    //属性值
    private String value;

    public String getAttrCd() {
        return attrCd;
    }

    public void setAttrCd(String attrCd) {
        this.attrCd = attrCd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
