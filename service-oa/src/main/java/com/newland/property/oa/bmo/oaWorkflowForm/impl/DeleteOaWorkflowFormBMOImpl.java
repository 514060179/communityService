package com.newland.property.oa.bmo.oaWorkflowForm.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.oa.IOaWorkflowFormInnerServiceSMO;
import com.newland.property.oa.bmo.oaWorkflowForm.IDeleteOaWorkflowFormBMO;
import com.newland.property.po.oaWorkflow.OaWorkflowFormPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOaWorkflowFormBMOImpl")
public class DeleteOaWorkflowFormBMOImpl implements IDeleteOaWorkflowFormBMO {

    @Autowired
    private IOaWorkflowFormInnerServiceSMO oaWorkflowFormInnerServiceSMOImpl;

    /**
     * @param oaWorkflowFormPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(OaWorkflowFormPo oaWorkflowFormPo) {

        int flag = oaWorkflowFormInnerServiceSMOImpl.deleteOaWorkflowForm(oaWorkflowFormPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
