package com.newland.property.oa.bmo.oaWorkflow.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.dto.oaWorkflow.OaWorkflowDto;
import com.newland.property.intf.oa.IOaWorkflowInnerServiceSMO;
import com.newland.property.oa.bmo.oaWorkflow.IUpdateOaWorkflowBMO;
import com.newland.property.po.oaWorkflow.OaWorkflowPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateOaWorkflowBMOImpl")
public class UpdateOaWorkflowBMOImpl implements IUpdateOaWorkflowBMO {

    @Autowired
    private IOaWorkflowInnerServiceSMO oaWorkflowInnerServiceSMOImpl;

    /**
     * @param oaWorkflowPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(OaWorkflowPo oaWorkflowPo) {
        //只要已修改 就 状态刷为待部署
        oaWorkflowPo.setState(OaWorkflowDto.STATE_WAIT);
        int flag = oaWorkflowInnerServiceSMOImpl.updateOaWorkflow(oaWorkflowPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
