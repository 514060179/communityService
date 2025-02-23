package com.newland.property.oa.bmo.oaWorkflowXml;
import com.newland.property.po.oaWorkflow.OaWorkflowXmlPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteOaWorkflowXmlBMO {


    /**
     * 修改OA流程图
     * add by wuxw
     * @param oaWorkflowXmlPo
     * @return
     */
    ResponseEntity<String> delete(OaWorkflowXmlPo oaWorkflowXmlPo);


}
