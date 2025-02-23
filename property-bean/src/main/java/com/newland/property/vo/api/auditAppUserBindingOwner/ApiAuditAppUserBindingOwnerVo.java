package com.newland.property.vo.api.auditAppUserBindingOwner;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiAuditAppUserBindingOwnerVo extends MorePageVo implements Serializable {
    List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners;


    public List<ApiAuditAppUserBindingOwnerDataVo> getAuditAppUserBindingOwners() {
        return auditAppUserBindingOwners;
    }

    public void setAuditAppUserBindingOwners(List<ApiAuditAppUserBindingOwnerDataVo> auditAppUserBindingOwners) {
        this.auditAppUserBindingOwners = auditAppUserBindingOwners;
    }
}
