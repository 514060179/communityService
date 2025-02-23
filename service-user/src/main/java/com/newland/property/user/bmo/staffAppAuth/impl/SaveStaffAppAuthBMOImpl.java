package com.newland.property.user.bmo.staffAppAuth.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IStaffAppAuthInnerServiceSMO;
import com.newland.property.po.user.StaffAppAuthPo;
import com.newland.property.user.bmo.staffAppAuth.ISaveStaffAppAuthBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStaffAppAuthBMOImpl")
public class SaveStaffAppAuthBMOImpl implements ISaveStaffAppAuthBMO {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param staffAppAuthPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(StaffAppAuthPo staffAppAuthPo) {

        staffAppAuthPo.setAuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auId));
        int flag = staffAppAuthInnerServiceSMOImpl.saveStaffAppAuth(staffAppAuthPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
