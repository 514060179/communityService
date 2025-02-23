package com.newland.property.community.cmd.room;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.fee.PayFeeDto;
import com.newland.property.dto.owner.OwnerRoomRelDto;
import com.newland.property.dto.unit.UnitDto;
import com.newland.property.intf.community.*;
import com.newland.property.intf.fee.IPayFeeDetailMonthInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeDetailV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.intf.report.IReportOweFeeInnerServiceSMO;
import com.newland.property.intf.user.IOwnerRoomRelV1InnerServiceSMO;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.owner.OwnerRoomRelPo;
import com.newland.property.po.payFee.PayFeeDetailMonthPo;
import com.newland.property.po.reportFee.ReportOweFeePo;
import com.newland.property.po.room.RoomPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmdDoc(title = "删除房屋",
        description = "对应后台 删除房屋功能，这里需要先解绑业主和房屋关系 才能做删除",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/room.deleteRoom",
        resource = "communityDoc",
        author = "吴学文",
        serviceCode = "room.deleteRoom",
        seq = 15
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "roomId", length = 30, remark = "单元ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "{\n" +
                "\t\"roomId\": \"123123123123\",\n" +
                "\t\"communityId\": \"2022121921870161\",\n" +
                "}",
        resBody = "{\"code\":0,\"msg\":\"成功\"}"
)
@NewlandPropertyCmd(serviceCode = "room.deleteRoom")
public class DeleteRoomCmd extends Cmd {

    @Autowired
    private IUnitV1InnerServiceSMO unitV1InnerServiceSMOImpl;
    @Autowired
    private IUnitInnerServiceSMO unitInnerServiceSMOImpl;
    @Autowired
    private IFloorV1InnerServiceSMO floorV1InnerServiceSMOImpl;


    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IRoomAttrV1InnerServiceSMO roomAttrV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelV1InnerServiceSMO ownerRoomRelV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailMonthInnerServiceSMO payFeeDetailMonthInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();
        Assert.jsonObjectHaveKey(reqJson, "roomId", "请求报文中未包含roomId节点");
        Assert.jsonObjectHaveKey(reqJson, "communityId", "请求报文中未包含communityId节点");
        Assert.jsonObjectHaveKey(reqJson, "unitId", "请求报文中未包含unitId节点");


        UnitDto unitDto = new UnitDto();
        unitDto.setCommunityId(reqJson.getString("communityId"));
        unitDto.setUnitId(reqJson.getString("unitId"));
        //校验小区楼ID和小区是否有对应关系
        List<UnitDto> units = unitInnerServiceSMOImpl.queryUnitsByCommunityId(unitDto);

        if (units == null || units.size() < 1) {
            throw new IllegalArgumentException("传入单元ID不是该小区的单元");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        JSONObject businessUnit = new JSONObject();
        businessUnit.putAll(reqJson);
        RoomPo roomPo = BeanConvertUtil.covertBean(businessUnit, RoomPo.class);
        int flag = roomV1InnerServiceSMOImpl.deleteRoom(roomPo);
        if (flag < 1) {
            throw new CmdException("删除房屋失败");
        }

        // todo 解绑业主
        unbindOwnerRoomRel(roomPo);

        // todo 删除房屋下的费用 和缴费记录
        deleteRoomFee(roomPo);


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }


    /**
     * 自动解绑 业主和房屋
     *
     * @param roomPo
     */
    private void unbindOwnerRoomRel(RoomPo roomPo) {
        if (StringUtil.isEmpty(roomPo.getRoomId())) {
            throw new CmdException("房屋ID不能为空");
        }

        OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
        ownerRoomRelDto.setRoomId(roomPo.getRoomId());
        List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelV1InnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);

        if (ownerRoomRelDtos == null || ownerRoomRelDtos.size() < 1) {
            return;
        }
        OwnerRoomRelPo ownerRoomRelPo = null;
        for (OwnerRoomRelDto tmpOwnerRoomRelDto : ownerRoomRelDtos) {
            ownerRoomRelPo = new OwnerRoomRelPo();
            ownerRoomRelPo.setRelId(tmpOwnerRoomRelDto.getRelId());
            ownerRoomRelV1InnerServiceSMOImpl.deleteOwnerRoomRel(ownerRoomRelPo);
        }
    }

    /**
     * 删除房屋费用
     *
     * @param roomPo
     */
    private void deleteRoomFee(RoomPo roomPo) {
        if (StringUtil.isEmpty(roomPo.getRoomId())) {
            throw new CmdException("房屋ID不能为空");
        }
        PayFeeDto feeDto = new PayFeeDto();
        feeDto.setPayerObjId(roomPo.getRoomId());
        feeDto.setPayerObjType(FeeDto.PAYER_OBJ_TYPE_ROOM);
        feeDto.setCommunityId(roomPo.getCommunityId());
        List<PayFeeDto> feeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(feeDto);

        if (feeDtos == null || feeDtos.size() < 1) {
            return;
        }

        for (PayFeeDto payFeeDto : feeDtos) {
            doDeleteFee(payFeeDto);
        }

    }

    /**
     * @param payFeeDto
     */
    private void doDeleteFee(PayFeeDto payFeeDto) {
        //todo 删除缴费记录

        PayFeeDetailPo payFeeDetailPo = new PayFeeDetailPo();
        payFeeDetailPo.setFeeId(payFeeDto.getFeeId());
        payFeeDetailPo.setCommunityId(payFeeDto.getCommunityId());
        payFeeDetailV1InnerServiceSMOImpl.deletePayFeeDetailNew(payFeeDetailPo);

        //todo 删除费用
        PayFeePo payFeePo = new PayFeePo();
        payFeePo.setFeeId(payFeeDto.getFeeId());
        payFeePo.setCommunityId(payFeeDto.getCommunityId());
        payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);

        //todo 删除欠费
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setFeeId(payFeeDto.getFeeId());
        reportOweFeePo.setCommunityId(payFeeDto.getCommunityId());
        reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);

        //todo 删除 离散月数据
        PayFeeDetailMonthPo payFeeDetailMonthPo = new PayFeeDetailMonthPo();
        payFeeDetailMonthPo.setFeeId(payFeeDto.getFeeId());
        payFeeDetailMonthPo.setCommunityId(payFeeDto.getCommunityId());
        payFeeDetailMonthInnerServiceSMOImpl.deletePayFeeDetailMonth(payFeeDetailMonthPo);

    }


}
