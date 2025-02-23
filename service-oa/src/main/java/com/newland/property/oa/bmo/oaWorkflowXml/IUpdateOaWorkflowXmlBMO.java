package com.newland.property.oa.bmo.oaWorkflowXml;
import com.newland.property.po.oaWorkflow.OaWorkflowXmlPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateOaWorkflowXmlBMO {


    /**
     * 修改OA流程图
     * add by wuxw
     * @param oaWorkflowXmlPo
     * @return
     */
    ResponseEntity<String> update(OaWorkflowXmlPo oaWorkflowXmlPo);


}
