package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.IWorkflowAuditMessageServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.oaWorkflow.WorkflowAuditMessageDto;
import com.newland.property.intf.common.IWorkflowAuditMessageInnerServiceSMO;
import com.newland.property.po.oaWorkflow.WorkflowAuditMessagePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 流程审核表内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class WorkflowAuditMessageInnerServiceSMOImpl extends BaseServiceSMO implements IWorkflowAuditMessageInnerServiceSMO {

    @Autowired
    private IWorkflowAuditMessageServiceDao workflowAuditMessageServiceDaoImpl;


    @Override
    public int saveWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo) {
        int saveFlag = 1;
        workflowAuditMessageServiceDaoImpl.saveWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessagePo));
        return saveFlag;
    }

    @Override
    public int updateWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo) {
        int saveFlag = 1;
        workflowAuditMessageServiceDaoImpl.updateWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessagePo));
        return saveFlag;
    }

    @Override
    public int deleteWorkflowAuditMessage(@RequestBody WorkflowAuditMessagePo workflowAuditMessagePo) {
        int saveFlag = 1;
        workflowAuditMessagePo.setStatusCd("1");
        workflowAuditMessageServiceDaoImpl.updateWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessagePo));
        return saveFlag;
    }

    @Override
    public List<WorkflowAuditMessageDto> queryWorkflowAuditMessages(@RequestBody WorkflowAuditMessageDto workflowAuditMessageDto) {

        //校验是否传了 分页信息

        int page = workflowAuditMessageDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            workflowAuditMessageDto.setPage((page - 1) * workflowAuditMessageDto.getRow());
        }

        List<WorkflowAuditMessageDto> workflowAuditMessages = BeanConvertUtil.covertBeanList(workflowAuditMessageServiceDaoImpl.getWorkflowAuditMessageInfo(BeanConvertUtil.beanCovertMap(workflowAuditMessageDto)), WorkflowAuditMessageDto.class);

        return workflowAuditMessages;
    }


    @Override
    public int queryWorkflowAuditMessagesCount(@RequestBody WorkflowAuditMessageDto workflowAuditMessageDto) {
        return workflowAuditMessageServiceDaoImpl.queryWorkflowAuditMessagesCount(BeanConvertUtil.beanCovertMap(workflowAuditMessageDto));
    }

    public IWorkflowAuditMessageServiceDao getWorkflowAuditMessageServiceDaoImpl() {
        return workflowAuditMessageServiceDaoImpl;
    }

    public void setWorkflowAuditMessageServiceDaoImpl(IWorkflowAuditMessageServiceDao workflowAuditMessageServiceDaoImpl) {
        this.workflowAuditMessageServiceDaoImpl = workflowAuditMessageServiceDaoImpl;
    }
}
