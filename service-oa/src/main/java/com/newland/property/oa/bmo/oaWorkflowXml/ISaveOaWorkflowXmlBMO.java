package com.newland.property.oa.bmo.oaWorkflowXml;

import com.newland.property.po.oaWorkflow.OaWorkflowXmlPo;
import org.springframework.http.ResponseEntity;
public interface ISaveOaWorkflowXmlBMO {


    /**
     * 添加OA流程图
     * add by wuxw
     * @param oaWorkflowXmlPo
     * @return
     */
    ResponseEntity<String> save(OaWorkflowXmlPo oaWorkflowXmlPo);


}
