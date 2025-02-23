package com.newland.property.oa.bmo.oaWorkflowXml.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.oa.IOaWorkflowXmlInnerServiceSMO;
import com.newland.property.oa.bmo.oaWorkflowXml.IUpdateOaWorkflowXmlBMO;
import com.newland.property.po.oaWorkflow.OaWorkflowXmlPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOaWorkflowXmlBMOImpl")
public class UpdateOaWorkflowXmlBMOImpl implements IUpdateOaWorkflowXmlBMO {

    @Autowired
    private IOaWorkflowXmlInnerServiceSMO oaWorkflowXmlInnerServiceSMOImpl;

    /**
     * @param oaWorkflowXmlPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(OaWorkflowXmlPo oaWorkflowXmlPo) {

        int flag = oaWorkflowXmlInnerServiceSMOImpl.updateOaWorkflowXml(oaWorkflowXmlPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
