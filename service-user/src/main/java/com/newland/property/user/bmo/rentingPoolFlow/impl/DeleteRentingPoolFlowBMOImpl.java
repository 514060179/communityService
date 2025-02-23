package com.newland.property.user.bmo.rentingPoolFlow.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.newland.property.po.renting.RentingPoolFlowPo;
import com.newland.property.user.bmo.rentingPoolFlow.IDeleteRentingPoolFlowBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRentingPoolFlowBMOImpl")
public class DeleteRentingPoolFlowBMOImpl implements IDeleteRentingPoolFlowBMO {

    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    /**
     * @param rentingPoolFlowPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(RentingPoolFlowPo rentingPoolFlowPo) {

        int flag = rentingPoolFlowInnerServiceSMOImpl.deleteRentingPoolFlow(rentingPoolFlowPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
