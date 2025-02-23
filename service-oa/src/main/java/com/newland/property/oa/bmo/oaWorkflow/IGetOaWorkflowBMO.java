package com.newland.property.oa.bmo.oaWorkflow;

import com.newland.property.dto.oaWorkflow.OaWorkflowDto;
import org.springframework.http.ResponseEntity;

public interface IGetOaWorkflowBMO {


    /**
     * 查询OA工作流
     * add by wuxw
     *
     * @param oaWorkflowDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowDto oaWorkflowDto);


}
