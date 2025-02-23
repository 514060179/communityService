package com.newland.property.fee.bmo.feeCollectionOrder.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeCollectionOrderDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.fee.bmo.feeCollectionOrder.ISaveFeeCollectionOrderBMO;
import com.newland.property.intf.fee.IFeeCollectionOrderInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.fee.FeeCollectionOrderPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("saveFeeCollectionOrderBMOImpl")
public class SaveFeeCollectionOrderBMOImpl implements ISaveFeeCollectionOrderBMO {

    @Autowired
    private IFeeCollectionOrderInnerServiceSMO feeCollectionOrderInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param feeCollectionOrderPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(FeeCollectionOrderPo feeCollectionOrderPo) {

        //查询用户ID
        UserDto userDto = new UserDto();
        userDto.setUserId(feeCollectionOrderPo.getStaffId());
        List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto);

        Assert.listOnlyOne(userDtos, "员工不存在");

        feeCollectionOrderPo.setOrderId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orderId));
        feeCollectionOrderPo.setCollectionName(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B) + "催缴单");
        feeCollectionOrderPo.setState(FeeCollectionOrderDto.STATE_WAIT);
        feeCollectionOrderPo.setStaffName(userDtos.get(0).getName());
        int flag = feeCollectionOrderInnerServiceSMOImpl.saveFeeCollectionOrder(feeCollectionOrderPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
