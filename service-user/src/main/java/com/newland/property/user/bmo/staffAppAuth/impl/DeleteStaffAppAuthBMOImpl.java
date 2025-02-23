package com.newland.property.user.bmo.staffAppAuth.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IStaffAppAuthInnerServiceSMO;
import com.newland.property.po.user.StaffAppAuthPo;
import com.newland.property.user.bmo.staffAppAuth.IDeleteStaffAppAuthBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteStaffAppAuthBMOImpl")
public class DeleteStaffAppAuthBMOImpl implements IDeleteStaffAppAuthBMO {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    /**
     * @param staffAppAuthPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(StaffAppAuthPo staffAppAuthPo) {

        int flag = staffAppAuthInnerServiceSMOImpl.deleteStaffAppAuth(staffAppAuthPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
