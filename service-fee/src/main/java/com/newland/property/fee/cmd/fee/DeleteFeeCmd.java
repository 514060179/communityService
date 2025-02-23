package com.newland.property.fee.cmd.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.payFeeRuleBill.PayFeeRuleBillDto;
import com.newland.property.fee.feeMonth.IPayFeeMonth;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.fee.*;
import com.newland.property.intf.report.IReportOweFeeInnerServiceSMO;
import com.newland.property.po.fee.PayFeeDetailPo;
import com.newland.property.po.fee.PayFeePo;
import com.newland.property.po.payFeeRuleBill.PayFeeRuleBillPo;
import com.newland.property.po.reportFee.ReportOweFeePo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "fee.deleteFee")
public class DeleteFeeCmd extends Cmd {

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;

    @Autowired
    private IPayFeeDetailV1InnerServiceSMO payFeeDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IPayFeeMonth payFeeMonthImpl;

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    @Autowired
    private IPayFeeRuleBillV1InnerServiceSMO payFeeRuleBillV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Environment.isDevEnv();

        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区ID");
        Assert.hasKeyAndValue(reqJson, "feeId", "未包含feeId");

        FeeDto feeDto = new FeeDto();
        feeDto.setCommunityId(reqJson.getString("communityId"));
        feeDto.setFeeId(reqJson.getString("feeId"));

        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);

        Assert.listOnlyOne(feeDtos, "未查询到费用信息 或查询到多条" + reqJson);


        String feeValidate = MappingCache.getValue("DELETE_FEE_VALIDATE");
        if ("ON".equals(feeValidate)) {
            return;
        }

        //判断是否已经存在 缴费记录
        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setCommunityId(reqJson.getString("communityId"));
        feeDetailDto.setFeeId(reqJson.getString("feeId"));
        feeDetailDto.setStates(new String[]{"1400", "1000"});
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
        //Assert.listOnlyOne(feeDetailDtos, "存在缴费记录，不能取消，如果需要取消，请先退费");

        if (feeDetailDtos != null && feeDetailDtos.size() > 0) {
            throw new IllegalArgumentException("存在缴费记录，不能取消，如果需要取消，请先退费");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        JSONObject businessUnit = new JSONObject();
        businessUnit.put("feeId", reqJson.getString("feeId"));
        businessUnit.put("communityId", reqJson.getString("communityId"));
        PayFeePo payFeePo = BeanConvertUtil.covertBean(businessUnit, PayFeePo.class);

        int flag = payFeeV1InnerServiceSMOImpl.deletePayFee(payFeePo);
        if (flag < 1) {
            throw new IllegalArgumentException("删除失败");
        }

        PayFeeDetailPo payFeeDetailPo = BeanConvertUtil.covertBean(businessUnit, PayFeeDetailPo.class);
        List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(BeanConvertUtil.covertBean(payFeeDetailPo, FeeDetailDto.class));
        if(feeDetailDtos != null && !feeDetailDtos.isEmpty()) {
            int flag2 = payFeeDetailV1InnerServiceSMOImpl.deletePayFeeDetailNew(payFeeDetailPo);
            if (flag2 < 1) {
                throw new IllegalArgumentException("删除失败");
            }
        }

        //todo 检查费用是否有账单数据 如果有直接删除
        PayFeeRuleBillDto payFeeRuleBillDto = new PayFeeRuleBillDto();
        payFeeRuleBillDto.setFeeId(payFeePo.getFeeId());
        payFeeRuleBillDto.setCommunityId(payFeePo.getCommunityId());
        List<PayFeeRuleBillDto> payFeeRuleBillDtos = payFeeRuleBillV1InnerServiceSMOImpl.queryPayFeeRuleBills(payFeeRuleBillDto);
        if(payFeeRuleBillDtos != null && !payFeeRuleBillDtos.isEmpty()) {
            PayFeeRuleBillPo payFeeRuleBillPo = new PayFeeRuleBillPo();
            payFeeRuleBillPo.setBillId(payFeeRuleBillDtos.get(0).getBillId());
            payFeeRuleBillPo.setCommunityId(payFeeRuleBillDtos.get(0).getCommunityId());
            payFeeRuleBillV1InnerServiceSMOImpl.deletePayFeeRuleBill(payFeeRuleBillPo);
        }
        // todo 删除欠费信息
        ReportOweFeePo reportOweFeePo = new ReportOweFeePo();
        reportOweFeePo.setFeeId(payFeePo.getFeeId());
        reportOweFeePo.setCommunityId(payFeePo.getCommunityId());
        reportOweFeeInnerServiceSMOImpl.deleteReportOweFee(reportOweFeePo);

        //todo 离散的月
        payFeeMonthImpl.deleteFeeMonth(payFeePo.getFeeId(),payFeePo.getCommunityId());
    }
}
