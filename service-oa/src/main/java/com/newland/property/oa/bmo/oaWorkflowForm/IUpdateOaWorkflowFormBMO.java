package com.newland.property.oa.bmo.oaWorkflowForm;
import com.newland.property.po.oaWorkflow.OaWorkflowFormPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOaWorkflowFormBMO {


    /**
     * 修改OA表单
     * add by wuxw
     * @param oaWorkflowFormPo
     * @return
     */
    ResponseEntity<String> update(OaWorkflowFormPo oaWorkflowFormPo);


}
