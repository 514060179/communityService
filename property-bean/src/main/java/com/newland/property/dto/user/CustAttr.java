package com.newland.property.dto.user;

import com.newland.property.dto.DefaultAttrEntity;

import java.util.Objects;

/**
 * 客户属性
 * Created by wuxw on 2016/12/27.
 */
public class CustAttr extends DefaultAttrEntity{

    private String custId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustAttr)) {
            return false;
        }
        CustAttr custAttr = (CustAttr) o;
        return Objects.equals(getCustId(), custAttr.getCustId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustId());
    }
}
