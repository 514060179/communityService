package com.newland.property.api.components.auditUser;

import com.newland.property.core.context.IPageData;
import com.newland.property.api.smo.auditUser.IAddAuditUserSMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * 添加添加审核人员组件
 */
@Component("addAuditUserStepBinding")
public class AddAuditUserStepBindingComponent {

    @Autowired
    private IAddAuditUserSMO addAuditUserSMOImpl;

    /**
     * 添加审核人员数据
     * @param pd 页面数据封装
     * @return ResponseEntity 对象
     */
    public ResponseEntity<String> binding(IPageData pd){
        return addAuditUserSMOImpl.saveAuditUser(pd);
    }

    public IAddAuditUserSMO getAddAuditUserSMOImpl() {
        return addAuditUserSMOImpl;
    }

    public void setAddAuditUserSMOImpl(IAddAuditUserSMO addAuditUserSMOImpl) {
        this.addAuditUserSMOImpl = addAuditUserSMOImpl;
    }
}
