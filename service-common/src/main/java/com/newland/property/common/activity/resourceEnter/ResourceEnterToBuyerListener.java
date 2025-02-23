package com.newland.property.common.activity.resourceEnter;

import com.newland.property.intf.common.IAuditUserInnerServiceSMO;
import com.newland.property.dto.audit.AuditUserDto;
import com.newland.property.dto.resource.ResourceOrderDto;
import com.newland.property.dto.audit.AuditUser;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.BeanConvertUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

/**
 * 采购人员采购
 */
public class ResourceEnterToBuyerListener implements TaskListener , ExecutionListener {

    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public void notify(DelegateTask delegateTask) {

        auditUserInnerServiceSMOImpl = ApplicationContextFactory.getBean("auditUserInnerServiceSMOImpl", IAuditUserInnerServiceSMO.class);
        AuditUserDto auditUserDto = new AuditUserDto();
        ResourceOrderDto resourceOrderDto = (ResourceOrderDto)delegateTask.getVariable("resourceOrderDto");
        auditUserDto.setStoreId(resourceOrderDto.getStoreId());
        auditUserDto.setObjCode("resourceEntry");
        auditUserDto.setAuditLink("809003");
        List<AuditUserDto> auditUserDtos = auditUserInnerServiceSMOImpl.queryAuditUsers(auditUserDto);

        for (AuditUserDto tmpAuditUser : auditUserDtos) {
            AuditUser auditUser = BeanConvertUtil.covertBean(tmpAuditUser, AuditUser.class);

            delegateTask.setVariable(auditUser.getUserId(), auditUser);

        }
    }

    @Override
    public void notify(DelegateExecution execution) {

    }
}
