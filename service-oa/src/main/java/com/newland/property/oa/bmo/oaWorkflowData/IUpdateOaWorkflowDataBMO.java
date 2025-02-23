package com.newland.property.oa.bmo.oaWorkflowData;
import com.newland.property.po.oaWorkflow.OaWorkflowDataPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOaWorkflowDataBMO {


    /**
     * 修改OA表单审批数据
     * add by wuxw
     * @param oaWorkflowDataPo
     * @return
     */
    ResponseEntity<String> update(OaWorkflowDataPo oaWorkflowDataPo);


}
