package com.newland.property.report.bmo.reportFeeYearCollectionDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.newland.property.po.reportFee.ReportFeeYearCollectionDetailPo;
import com.newland.property.report.bmo.reportFeeYearCollectionDetail.IUpdateReportFeeYearCollectionDetailBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateReportFeeYearCollectionDetailBMOImpl")
public class UpdateReportFeeYearCollectionDetailBMOImpl implements IUpdateReportFeeYearCollectionDetailBMO {

    @Autowired
    private IReportFeeYearCollectionDetailInnerServiceSMO reportFeeYearCollectionDetailInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo) {

        int flag = reportFeeYearCollectionDetailInnerServiceSMOImpl.updateReportFeeYearCollectionDetail(reportFeeYearCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
