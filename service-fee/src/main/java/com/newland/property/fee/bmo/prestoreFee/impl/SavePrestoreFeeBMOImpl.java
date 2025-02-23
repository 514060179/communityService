package com.newland.property.fee.bmo.prestoreFee.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.fee.bmo.prestoreFee.ISavePrestoreFeeBMO;
import com.newland.property.intf.fee.IPrestoreFeeInnerServiceSMO;
import com.newland.property.po.prestoreFee.PrestoreFeePo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("savePrestoreFeeBMOImpl")
public class SavePrestoreFeeBMOImpl implements ISavePrestoreFeeBMO {

    @Autowired
    private IPrestoreFeeInnerServiceSMO prestoreFeeInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param prestoreFeePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(PrestoreFeePo prestoreFeePo) {

        prestoreFeePo.setPrestoreFeeId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_prestoreFeeId));
        int flag = prestoreFeeInnerServiceSMOImpl.savePrestoreFee(prestoreFeePo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
