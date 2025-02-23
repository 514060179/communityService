package com.newland.property.oa.bmo.oaWorkflow;

import com.newland.property.po.oaWorkflow.OaWorkflowPo;
import org.springframework.http.ResponseEntity;
public interface ISaveOaWorkflowBMO {


    /**
     * 添加OA工作流
     * add by wuxw
     * @param oaWorkflowPo
     * @return
     */
    ResponseEntity<String> save(OaWorkflowPo oaWorkflowPo);


}
