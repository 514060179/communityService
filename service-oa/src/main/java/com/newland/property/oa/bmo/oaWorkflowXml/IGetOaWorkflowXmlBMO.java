package com.newland.property.oa.bmo.oaWorkflowXml;
import com.newland.property.dto.oaWorkflow.OaWorkflowXmlDto;
import org.springframework.http.ResponseEntity;
public interface IGetOaWorkflowXmlBMO {


    /**
     * 查询OA流程图
     * add by wuxw
     * @param  oaWorkflowXmlDto
     * @return
     */
    ResponseEntity<String> get(OaWorkflowXmlDto oaWorkflowXmlDto);


}
