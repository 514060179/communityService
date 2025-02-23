package com.newland.property.oa.bmo.oaWorkflowData;
import com.newland.property.po.oaWorkflow.OaWorkflowDataPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowDataBMO {


    /**
     * 修改OA表单审批数据
     * add by wuxw
     * @param oaWorkflowDataPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowDataPo oaWorkflowDataPo);


}
