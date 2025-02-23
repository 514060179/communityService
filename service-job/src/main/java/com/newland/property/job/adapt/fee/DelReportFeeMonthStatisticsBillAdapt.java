package com.newland.property.job.adapt.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.ReportFeeMonthStatisticsPrepaymentDto.ReportFeeMonthStatisticsPrepaymentDto;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.payFee.ReturnPayFeeDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.fee.IReturnPayFeeInnerServiceSMO;
import com.newland.property.intf.report.IReportFeeMonthStatisticsPrepaymentInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.po.ReportFeeMonthStatisticsPrepaymentPo.ReportFeeMonthStatisticsPrepaymentPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * author fqz
 *
 * @description 退费审核通过删除账单明细表信息
 * @date 2023-05-27 16:20
 */
@Component(value = "delReportFeeMonthStatisticsBillAdapt")
public class DelReportFeeMonthStatisticsBillAdapt extends DatabusAdaptImpl {

    @Autowired
    private IReturnPayFeeInnerServiceSMO returnPayFeeInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IReportFeeMonthStatisticsPrepaymentInnerServiceSMO reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();
        //获取费用id
        String feeId = data.getString("feeId");
        if (StringUtil.isEmpty(feeId)) {
            return;
        }
        ReturnPayFeeDto returnPayFeeDto = new ReturnPayFeeDto();
        returnPayFeeDto.setFeeId(feeId);
        List<ReturnPayFeeDto> returnPayFeeDtos = returnPayFeeInnerServiceSMOImpl.queryReturnPayFees(returnPayFeeDto);
        if (returnPayFeeDtos != null && returnPayFeeDtos.size() == 1) {
            delReportFeeMonthStatisticsBill(business, returnPayFeeDtos);
        }
    }

    private void delReportFeeMonthStatisticsBill(Business business, List<ReturnPayFeeDto> returnPayFeeDtos) {
        //获取退费状态
        String state = returnPayFeeDtos.get(0).getState();
        if (!StringUtil.isEmpty(state) && "1100".equals(state)) { //1000 审核中；1100 审核通过；1200 审核未通过；1300 退款单
            //获取缴费明细id
            String detailId = returnPayFeeDtos.get(0).getDetailId();
            if (StringUtil.isEmpty(detailId)) {
                return;
            }
            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setDetailId(detailId);
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);
            Assert.listOnlyOne(feeDetailDtos, "查询费用明细错误！");
            ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepaymentDto = new ReportFeeMonthStatisticsPrepaymentDto();
            reportFeeMonthStatisticsPrepaymentDto.setFeeId(returnPayFeeDtos.get(0).getFeeId());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            reportFeeMonthStatisticsPrepaymentDto.setFeeBeginTime(format.format(feeDetailDtos.get(0).getStartTime()));
            reportFeeMonthStatisticsPrepaymentDto.setFeeFinishTime(format.format(feeDetailDtos.get(0).getEndTime()));
            List<ReportFeeMonthStatisticsPrepaymentDto> reportFeeMonthStatisticsPrepaymentDtos = reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.queryReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentDto);
            if (reportFeeMonthStatisticsPrepaymentDtos != null && reportFeeMonthStatisticsPrepaymentDtos.size() > 0) {
                for (ReportFeeMonthStatisticsPrepaymentDto reportFeeMonthStatisticsPrepayment : reportFeeMonthStatisticsPrepaymentDtos) {
                    ReportFeeMonthStatisticsPrepaymentPo reportFeeMonthStatisticsPrepaymentPo = new ReportFeeMonthStatisticsPrepaymentPo();
                    reportFeeMonthStatisticsPrepaymentPo.setPrepaymentId(reportFeeMonthStatisticsPrepayment.getPrepaymentId());
                    reportFeeMonthStatisticsPrepaymentInnerServiceSMOImpl.deleteReportFeeMonthStatisticsPrepayment(reportFeeMonthStatisticsPrepaymentPo);
                }
            }
        }
    }
}
