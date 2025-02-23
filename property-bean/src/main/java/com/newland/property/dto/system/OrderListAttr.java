package com.newland.property.dto.system;

import java.util.Date;
import java.util.Objects;

/**
 * 购物车属性表
 * Created by wuxw on 2017/4/9.
 */
public class OrderListAttr {

    //购物车ID
    private String olId;

    //属性编码，对应 Attr 表
    private String attrCd;
    //属性编码对应值
    private String value;
    //名称
    private String name;

    //创建时间
    private Date create_dt;

    public String getOlId() {
        return olId;
    }

    public void setOlId(String olId) {
        this.olId = olId;
    }

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

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderListAttr)) {
            return false;
        }
        OrderListAttr that = (OrderListAttr) o;
        return Objects.equals(getOlId(), that.getOlId()) && Objects.equals(getAttrCd(), that.getAttrCd()) && Objects.equals(getValue(), that.getValue()) && Objects.equals(getName(), that.getName()) && Objects.equals(getCreate_dt(), that.getCreate_dt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOlId(), getAttrCd(), getValue(), getName(), getCreate_dt());
    }
}
