package com.newland.property.fee.cmd.oweFeeCallable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.dto.oweFeeCallable.OweFeeCallableDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.reportFee.ReportOweFeeDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.fee.IOweFeeCallableV1InnerServiceSMO;
import com.newland.property.intf.report.IReportOweFeeInnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.po.oweFeeCallable.OweFeeCallablePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * 登记催缴记录
 */
@NewlandPropertyCmd(serviceCode = "oweFeeCallable.writeOweFeeCallable")
public class WriteOweFeeCallableCmd extends Cmd {

    @Autowired
    private IOweFeeCallableV1InnerServiceSMO oweFeeCallableV1InnerServiceSMOImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
        Assert.hasKeyAndValue(reqJson, "roomId", "未包含房屋");

        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        if (feeIds == null || feeIds.size() < 1) {
            throw new CmdException("未包含费用");
        }

    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        String staffId = context.getReqHeaders().get("user-id");

        UserDto userDto = new UserDto();
        userDto.setUserId(staffId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "员工不在");

        JSONArray feeIds = reqJson.getJSONArray("feeIds");

        for (int feeIndex = 0; feeIndex < feeIds.size(); feeIndex++) {
            doWriteFee(feeIds.getString(feeIndex), reqJson, userDtos.get(0));
        }


    }

    private void doWriteFee(String feeId, JSONObject reqJson, UserDto userDto) {


        ReportOweFeeDto reportOweFeeDto = new ReportOweFeeDto();
        reportOweFeeDto.setFeeId(feeId);
        reportOweFeeDto.setCommunityId(reqJson.getString("communityId"));
        List<ReportOweFeeDto> reportOweFeeDtos = reportOweFeeInnerServiceSMOImpl.queryReportAllOweFees(reportOweFeeDto);
        Assert.listOnlyOne(reportOweFeeDtos, "欠费不存在");

        String ownerId = reportOweFeeDtos.get(0).getOwnerId();
        String ownerName = reportOweFeeDtos.get(0).getOwnerName();

        if(StringUtil.isEmpty(ownerId)){
            ownerId = "-1";
        }

        if(StringUtil.isEmpty(ownerName)){
            ownerName = "无业主";
        }

        //todo
        OweFeeCallablePo oweFeeCallablePo = new OweFeeCallablePo();

        oweFeeCallablePo.setAmountdOwed(reportOweFeeDtos.get(0).getAmountOwed());
        oweFeeCallablePo.setCallableWay(OweFeeCallableDto.CALLABLE_WAY_PRINT);
        oweFeeCallablePo.setOfcId(GenerateCodeFactory.getGeneratorId("11"));
        oweFeeCallablePo.setFeeId(reportOweFeeDtos.get(0).getFeeId());
        oweFeeCallablePo.setFeeName(reportOweFeeDtos.get(0).getFeeName());
        oweFeeCallablePo.setCommunityId(reqJson.getString("communityId"));
        oweFeeCallablePo.setConfigId(reportOweFeeDtos.get(0).getConfigId());
        oweFeeCallablePo.setOwnerId(ownerId);
        oweFeeCallablePo.setOwnerName(ownerName);
        oweFeeCallablePo.setPayerObjId(reportOweFeeDtos.get(0).getPayerObjId());
        oweFeeCallablePo.setPayerObjName(reportOweFeeDtos.get(0).getPayerObjName());
        oweFeeCallablePo.setPayerObjType(reportOweFeeDtos.get(0).getPayerObjType());
        oweFeeCallablePo.setRemark(reqJson.getString("remark"));
        oweFeeCallablePo.setStaffId(userDto.getUserId());
        oweFeeCallablePo.setStaffName(userDto.getName());
        oweFeeCallablePo.setState(OweFeeCallableDto.STATE_COMPLETE);
        oweFeeCallablePo.setStartTime(reportOweFeeDtos.get(0).getEndTime());
        oweFeeCallablePo.setEndTime(reportOweFeeDtos.get(0).getDeadlineTime());

        int flag = oweFeeCallableV1InnerServiceSMOImpl.saveOweFeeCallable(oweFeeCallablePo);

        if (flag < 1) {
            throw new CmdException("登记失败");
        }

        // TODO: 登记欠费催缴发推送-jpush
        // appPage: pages/fee/oweFee

        String memberId = "";
        JSONArray fees = reqJson.getJSONArray("fees");
        for (int feeIndex = 0; feeIndex < fees.size(); feeIndex++) {
            if(feeId.equals(fees.getJSONObject(feeIndex).getString("feeId"))){
                JSONArray feeAttrs = fees.getJSONObject(feeIndex).getJSONArray("feeAttrs");
                for(int i =0;i < feeAttrs.size(); i++){
                    if("业主ID".equals(feeAttrs.getJSONObject(i).getString("specCdName"))){
                        memberId = feeAttrs.getJSONObject(i).getString("value");
                    }
                }
            }
        }

        if(!StringUtil.isEmpty(memberId)) {
            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setMemberId(memberId);
            List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

            UserDto userDto1 = new UserDto();
            userDto1.setTel(ownerDtos.get(0).getLink());
            userDto1.setStatusCd("0");
            List<UserDto> userDtos = userInnerServiceSMOImpl.getUsers(userDto1);

            JPushMessageDto messageDto = new JPushMessageDto();
            messageDto.setCommunityId(reqJson.getString("communityId"));
            messageDto.setUserId(userDtos.get(0).getUserId());
            messageDto.setPageUrl("/pages/fee/oweFee");
            messageDto.setTitle("催缴信息");
            messageDto.setContent("您有尚未缴清的费用，请及时缴费");
            messageDto.setPageType("appPage");
            JPushMessageQueue.addMsg(messageDto);
        }
    }
}
