package com.newland.property.oa.bmo.oaWorkflowData.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.oa.IOaWorkflowDataInnerServiceSMO;
import com.newland.property.oa.bmo.oaWorkflowData.ISaveOaWorkflowDataBMO;
import com.newland.property.po.oaWorkflow.OaWorkflowDataPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveOaWorkflowDataBMOImpl")
public class SaveOaWorkflowDataBMOImpl implements ISaveOaWorkflowDataBMO {

    @Autowired
    private IOaWorkflowDataInnerServiceSMO oaWorkflowDataInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param oaWorkflowDataPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(OaWorkflowDataPo oaWorkflowDataPo) {

        oaWorkflowDataPo.setDataId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_dataId));
        int flag = oaWorkflowDataInnerServiceSMOImpl.saveOaWorkflowData(oaWorkflowDataPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
