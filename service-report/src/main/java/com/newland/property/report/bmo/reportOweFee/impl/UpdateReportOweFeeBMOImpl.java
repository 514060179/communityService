package com.newland.property.report.bmo.reportOweFee.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportOweFeeInnerServiceSMO;
import com.newland.property.po.reportFee.ReportOweFeePo;
import com.newland.property.report.bmo.reportOweFee.IUpdateReportOweFeeBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportOweFeeBMOImpl")
public class UpdateReportOweFeeBMOImpl implements IUpdateReportOweFeeBMO {

    @Autowired
    private IReportOweFeeInnerServiceSMO reportOweFeeInnerServiceSMOImpl;

    /**
     * @param reportOweFeePo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportOweFeePo reportOweFeePo) {

        int flag = reportOweFeeInnerServiceSMOImpl.updateReportOweFee(reportOweFeePo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
