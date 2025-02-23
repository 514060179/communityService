package com.newland.property.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeAttrDto;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.fee.feeMonth.IPayFeeMonth;
import com.newland.property.fee.smo.impl.FeeAttrInnerServiceSMOImpl;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.po.fee.FeeAttrPo;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "fee.updateFee")
public class UpdateFeeCmd extends Cmd {


    private static final int DEFAULT_ADD_FEE_COUNT = 200;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private FeeAttrInnerServiceSMOImpl feeAttrInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        // super.validatePageInfo(pd);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "feeId", "未包含feeId");
        Assert.hasKeyAndValue(reqJson, "startTime", "未包含开始时间");
        Assert.hasKeyAndValue(reqJson, "endTime", "未包含结束时间");

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setFeeId(reqJson.getString("feeId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "未查询到费用信息 或查询到多条" + reqJson);

        if(FeeDto.FEE_FLAG_CYCLE.equals(feeDtos.get(0).getFeeFlag()) && reqJson.containsKey("maxEndTime")){
            reqJson.remove("maxEndTime");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        PayFeePo payFeePo = BeanConvertUtil.covertBean(reqJson, PayFeePo.class);
        int flag = payFeeV1InnerServiceSMOImpl.updatePayFee(payFeePo);

        if (flag < 1) {
            throw new CmdException("修改费用");
        }

        // todo 重新计算离散月
        payFeeMonthImpl.deleteFeeMonth(payFeePo.getFeeId(),payFeePo.getCommunityId());
        payFeeMonthImpl.doGeneratorOrRefreshFeeMonth(payFeePo.getFeeId(),payFeePo.getCommunityId());

        // todo 欠费重新生成
        List<String> feeIds= new ArrayList<>();
        feeIds.add(payFeePo.getFeeId());
        payFeeMonthImpl.doGeneratorOweFees(feeIds,payFeePo.getCommunityId());


        if (reqJson.containsKey("maxEndTime") && !StringUtil.isEmpty(reqJson.getString("maxEndTime"))) {
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setFeeId(payFeePo.getFeeId());
            feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_ONCE_FEE_DEADLINE_TIME);
            feeAttrPo.setValue(reqJson.getString("maxEndTime"));
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            } else {
                feeAttrInnerServiceSMOImpl.updateFeeAttr(feeAttrPo);
            }
        }

        if (!reqJson.containsKey("computingFormula")
                || !FeeConfigDto.COMPUTING_FORMULA_RANT_RATE.equals(reqJson.getString("computingFormula"))) {
            return;
        }

        if (reqJson.containsKey("rate")) {
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setFeeId(payFeePo.getFeeId());
            feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_RATE);
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_RATE);
            feeAttrPo.setValue(reqJson.getString("rate"));
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            } else {
                feeAttrInnerServiceSMOImpl.updateFeeAttr(feeAttrPo);
            }
        }

        if (reqJson.containsKey("rateCycle")) {
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setFeeId(payFeePo.getFeeId());
            feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_RATE_CYCLE);
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_RATE_CYCLE);
            feeAttrPo.setValue(reqJson.getString("rateCycle"));
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            } else {
                feeAttrInnerServiceSMOImpl.updateFeeAttr(feeAttrPo);
            }
        }

        if (reqJson.containsKey("rateStartTime")) {
            FeeAttrDto feeAttrDto = new FeeAttrDto();
            feeAttrDto.setFeeId(payFeePo.getFeeId());
            feeAttrDto.setSpecCd(FeeAttrDto.SPEC_CD_RATE_START_TIME);
            List<FeeAttrDto> feeAttrDtos = feeAttrInnerServiceSMOImpl.queryFeeAttrs(feeAttrDto);
            FeeAttrPo feeAttrPo = new FeeAttrPo();
            feeAttrPo.setFeeId(payFeePo.getFeeId());
            feeAttrPo.setSpecCd(FeeAttrDto.SPEC_CD_RATE_START_TIME);
            feeAttrPo.setValue(reqJson.getString("rateStartTime"));
            feeAttrPo.setCommunityId(reqJson.getString("communityId"));
            if (feeAttrDtos == null || feeAttrDtos.size() < 1) {
                feeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
                feeAttrInnerServiceSMOImpl.saveFeeAttr(feeAttrPo);
            } else {
                feeAttrInnerServiceSMOImpl.updateFeeAttr(feeAttrPo);
            }
        }
    }
}
