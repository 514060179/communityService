package com.newland.property.common.bmo.hcGovTranslateDetail.impl;

import com.newland.property.common.bmo.hcGovTranslateDetail.ISaveHcGovTranslateDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IHcGovTranslateDetailInnerServiceSMO;
import com.newland.property.po.hcGovTranslate.HcGovTranslateDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveHcGovTranslateDetailBMOImpl")
public class SaveHcGovTranslateDetailBMOImpl implements ISaveHcGovTranslateDetailBMO {

    @Autowired
    private IHcGovTranslateDetailInnerServiceSMO hcGovTranslateDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param hcGovTranslateDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(HcGovTranslateDetailPo hcGovTranslateDetailPo) {

        hcGovTranslateDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = hcGovTranslateDetailInnerServiceSMOImpl.saveHcGovTranslateDetail(hcGovTranslateDetailPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
