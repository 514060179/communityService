package com.newland.property.common.bmo.hcGovTranslateDetail.impl;

import com.newland.property.common.bmo.hcGovTranslateDetail.IDeleteHcGovTranslateDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IHcGovTranslateDetailInnerServiceSMO;
import com.newland.property.po.hcGovTranslate.HcGovTranslateDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteHcGovTranslateDetailBMOImpl")
public class DeleteHcGovTranslateDetailBMOImpl implements IDeleteHcGovTranslateDetailBMO {

    @Autowired
    private IHcGovTranslateDetailInnerServiceSMO hcGovTranslateDetailInnerServiceSMOImpl;

    /**
     * @param hcGovTranslateDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(HcGovTranslateDetailPo hcGovTranslateDetailPo) {

        int flag = hcGovTranslateDetailInnerServiceSMOImpl.deleteHcGovTranslateDetail(hcGovTranslateDetailPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
