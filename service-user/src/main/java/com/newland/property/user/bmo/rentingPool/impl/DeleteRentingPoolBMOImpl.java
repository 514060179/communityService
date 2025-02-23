package com.newland.property.user.bmo.rentingPool.impl;

import com.newland.property.core.annotation.PropertyTransactional;

import com.newland.property.intf.user.IRentingPoolInnerServiceSMO;
import com.newland.property.po.renting.RentingPoolPo;
import com.newland.property.user.bmo.rentingPool.IDeleteRentingPoolBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteRentingPoolBMOImpl")
public class DeleteRentingPoolBMOImpl implements IDeleteRentingPoolBMO {

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;

    /**
     * @param rentingPoolPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(RentingPoolPo rentingPoolPo) {

        int flag = rentingPoolInnerServiceSMOImpl.deleteRentingPool(rentingPoolPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
