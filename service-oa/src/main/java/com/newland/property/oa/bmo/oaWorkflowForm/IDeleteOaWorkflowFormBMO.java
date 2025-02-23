package com.newland.property.oa.bmo.oaWorkflowForm;
import com.newland.property.po.oaWorkflow.OaWorkflowFormPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowFormBMO {


    /**
     * 修改OA表单
     * add by wuxw
     * @param oaWorkflowFormPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowFormPo oaWorkflowFormPo);


}
