package com.newland.property.common.activity.complaint;

import com.newland.property.intf.common.IAuditUserInnerServiceSMO;
import com.newland.property.dto.audit.AuditUserDto;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.dto.audit.AuditUser;
import com.newland.property.utils.factory.ApplicationContextFactory;
import com.newland.property.utils.util.BeanConvertUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

import java.util.List;

/**
 * 采购人员采购
 */
public class ComplaintUserListener implements TaskListener , ExecutionListener {
    private final static Logger logger = LoggerFactory.getLogger(ComplaintUserListener.class);

    private IAuditUserInnerServiceSMO auditUserInnerServiceSMOImpl;

    @Override
    public void notify(DelegateTask delegateTask) {

        auditUserInnerServiceSMOImpl = ApplicationContextFactory.getBean("auditUserInnerServiceSMOImpl", IAuditUserInnerServiceSMO.class);
        AuditUserDto auditUserDto = new AuditUserDto();
        ComplaintDto complaintDto = (ComplaintDto) delegateTask.getVariable("complaintDto");
        auditUserDto.setStoreId(complaintDto.getStoreId());
        auditUserDto.setObjCode("complaint");
        auditUserDto.setAuditLink("809004");
        List<AuditUserDto> auditUserDtos = auditUserInnerServiceSMOImpl.queryAuditUsers(auditUserDto);


        for (AuditUserDto tmpAuditUser : auditUserDtos) {
            AuditUser auditUser = BeanConvertUtil.covertBean(tmpAuditUser, AuditUser.class);
            delegateTask.setVariable(auditUser.getUserId(), auditUser);
        }

        if (auditUserDtos == null || auditUserDtos.size() < 1) {
            return;
        }
        //delegateTask.addCandidateUser(auditUserDtos.get(0).getUserId());
        logger.info("开始设置投诉建议审核人员："+auditUserDtos.get(0).getUserId());

        delegateTask.setAssignee(auditUserDtos.get(0).getUserId());
    }

    @Override
    public void notify(DelegateExecution execution) {

    }
}
