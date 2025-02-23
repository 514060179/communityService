package com.newland.property.job.bmo.businessDatabus.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.job.IBusinessDatabusInnerServiceSMO;
import com.newland.property.job.bmo.businessDatabus.IUpdateBusinessDatabusBMO;
import com.newland.property.po.business.BusinessDatabusPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateBusinessDatabusBMOImpl")
public class UpdateBusinessDatabusBMOImpl implements IUpdateBusinessDatabusBMO {

    @Autowired
    private IBusinessDatabusInnerServiceSMO businessDatabusInnerServiceSMOImpl;

    /**
     * @param businessDatabusPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(BusinessDatabusPo businessDatabusPo) {

        int flag = businessDatabusInnerServiceSMOImpl.updateBusinessDatabus(businessDatabusPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
