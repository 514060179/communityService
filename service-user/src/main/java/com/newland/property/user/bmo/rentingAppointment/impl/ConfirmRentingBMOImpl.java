package com.newland.property.user.bmo.rentingAppointment.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.renting.RentingAppointmentDto;
import com.newland.property.dto.renting.RentingPoolDto;
import com.newland.property.dto.renting.RentingPoolFlowDto;
import com.newland.property.intf.user.IRentingAppointmentInnerServiceSMO;
import com.newland.property.intf.user.IRentingPoolFlowInnerServiceSMO;
import com.newland.property.intf.user.IRentingPoolInnerServiceSMO;
import com.newland.property.po.renting.RentingAppointmentPo;
import com.newland.property.po.renting.RentingPoolPo;
import com.newland.property.po.renting.RentingPoolFlowPo;
import com.newland.property.user.bmo.rentingAppointment.IConfirmRentingBMO;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("confirmRentingBMOImpl")
public class ConfirmRentingBMOImpl implements IConfirmRentingBMO {

    @Autowired
    private IRentingAppointmentInnerServiceSMO rentingAppointmentInnerServiceSMOImpl;

    @Autowired
    private IRentingPoolInnerServiceSMO rentingPoolInnerServiceSMOImpl;


    @Autowired
    private IRentingPoolFlowInnerServiceSMO rentingPoolFlowInnerServiceSMOImpl;

    /**
     * @param rentingAppointmentPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> confirm(RentingAppointmentPo rentingAppointmentPo) {

        //查询 预约数据 校验
        RentingAppointmentDto rentingAppointmentDto = new RentingAppointmentDto();
        rentingAppointmentDto.setAppointmentId(rentingAppointmentPo.getAppointmentId());
        List<RentingAppointmentDto> rentingAppointmentDtos = rentingAppointmentInnerServiceSMOImpl.queryRentingAppointments(rentingAppointmentDto);

        Assert.listOnlyOne(rentingAppointmentDtos, "未找到预约信息");

        //校验 房源信息
        RentingPoolDto rentingPoolDto = new RentingPoolDto();
        rentingPoolDto.setRentingId(rentingAppointmentPo.getRentingId());
        List<RentingPoolDto> rentingPoolDtos = rentingPoolInnerServiceSMOImpl.queryRentingPools(rentingPoolDto);

        Assert.listOnlyOne(rentingPoolDtos, "未找到房源信息");

        //预约数据修改 租房成功
        rentingAppointmentPo.setState(RentingAppointmentDto.STATE_SUCCESS);
        rentingAppointmentPo.setRoomId(rentingPoolDtos.get(0).getRoomId());
        int flag = rentingAppointmentInnerServiceSMOImpl.updateRentingAppointment(rentingAppointmentPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        //房源状态修改 待支付
        RentingPoolPo rentingPoolPo = new RentingPoolPo();
        rentingPoolPo.setRentingId(rentingPoolDtos.get(0).getRentingId());
        rentingPoolPo.setState(RentingPoolDto.STATE_TO_PAY);
        flag = rentingPoolInnerServiceSMOImpl.updateRentingPool(rentingPoolPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改房屋状态失败");
        }

        //流程中插入租客信息
        RentingPoolFlowPo rentingPoolFlowPo = new RentingPoolFlowPo();
        rentingPoolFlowPo.setUserTel(rentingAppointmentDtos.get(0).getTenantTel());
        rentingPoolFlowPo.setUseName(rentingAppointmentDtos.get(0).getTenantName());
        rentingPoolFlowPo.setUserRole("2"); //租客
        rentingPoolFlowPo.setState(RentingPoolFlowDto.STATE_CONFIRM_RENTING);
        rentingPoolFlowPo.setRentingId(rentingAppointmentPo.getRentingId());
        rentingPoolFlowPo.setDealTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        rentingPoolFlowPo.setFlowId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_flowId));
        rentingPoolFlowPo.setContext("确认租房");
        rentingPoolFlowPo.setCommunityId(rentingPoolDtos.get(0).getCommunityId());
        flag = rentingPoolFlowInnerServiceSMOImpl.saveRentingPoolFlow(rentingPoolFlowPo);
        if (flag < 1) {
            throw new IllegalArgumentException("修改房屋状态失败");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
