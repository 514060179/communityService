package com.newland.property.oa.bmo.oaWorkflowData.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.newland.property.oa.bmo.oaWorkflowData.IDeleteOaWorkflowDataBMO;
import com.newland.property.po.oaWorkflow.OaWorkflowDataPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteOaWorkflowDataBMOImpl")
public class DeleteOaWorkflowDataBMOImpl implements IDeleteOaWorkflowDataBMO {

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;

    /**
     * @param oaWorkflowDataPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(OaWorkflowDataPo oaWorkflowDataPo) {

        int flag = oaWorkflowDataInnerServiceSMOImpl.deleteOaWorkflowData(oaWorkflowDataPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
