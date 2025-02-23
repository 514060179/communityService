package com.newland.property.oa.bmo.oaWorkflowData.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.newland.property.oa.bmo.oaWorkflowData.IUpdateOaWorkflowDataBMO;
import com.newland.property.po.oaWorkflow.OaWorkflowDataPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOaWorkflowDataBMOImpl")
public class UpdateOaWorkflowDataBMOImpl implements IUpdateOaWorkflowDataBMO {

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;

    /**
     * @param oaWorkflowDataPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(OaWorkflowDataPo oaWorkflowDataPo) {

        int flag = oaWorkflowDataInnerServiceSMOImpl.updateOaWorkflowData(oaWorkflowDataPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
