package com.newland.property.user.bmo.rentingPoolFlow.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.newland.property.po.renting.RentingPoolFlowPo;
import com.newland.property.user.bmo.rentingPoolFlow.ISaveRentingPoolFlowBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveRentingPoolFlowBMOImpl")
public class SaveRentingPoolFlowBMOImpl implements ISaveRentingPoolFlowBMO {

    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param rentingPoolFlowPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(RentingPoolFlowPo rentingPoolFlowPo) {

        rentingPoolFlowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        int flag = rentingPoolFlowInnerServiceSMOImpl.saveRentingPoolFlow(rentingPoolFlowPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
