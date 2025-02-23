package com.newland.property.user.bmo.staffAppAuth.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.user.StaffAppAuthDto;
import com.newland.property.intf.user.IStaffAppAuthInnerServiceSMO;
import com.newland.property.po.user.StaffAppAuthPo;
import com.newland.property.user.bmo.staffAppAuth.IUpdateStaffAppAuthBMO;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service("updateStaffAppAuthBMOImpl")
public class UpdateStaffAppAuthBMOImpl implements IUpdateStaffAppAuthBMO {

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    /**
     * @param staffAppAuthPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(StaffAppAuthPo staffAppAuthPo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
        staffAppAuthDto.setAppType(staffAppAuthPo.getAppType());
        staffAppAuthDto.setStaffId(staffAppAuthPo.getStaffId());
        staffAppAuthDto.setStoreId(staffAppAuthPo.getStoreId());
       // staffAppAuthDto.setOpenName(staffAppAuthPo.getOpenName());
        int count = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuthsCount(staffAppAuthDto);
        int flag = 0;
        if (count > 0) {
            List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
            Assert.listOnlyOne(staffAppAuthDtos, "查询到多个员工认证信息");
            staffAppAuthPo.setAuId(staffAppAuthDtos.get(0).getAuId());
            staffAppAuthPo.setCreateTime(format.format(new Date()));
            flag = staffAppAuthInnerServiceSMOImpl.updateStaffAppAuth(staffAppAuthPo);
        } else {
            staffAppAuthPo.setAuId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auId));
            flag = staffAppAuthInnerServiceSMOImpl.saveStaffAppAuth(staffAppAuthPo);
        }

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
