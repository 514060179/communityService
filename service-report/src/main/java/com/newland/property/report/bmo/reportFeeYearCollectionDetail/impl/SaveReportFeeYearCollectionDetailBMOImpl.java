package com.newland.property.report.bmo.reportFeeYearCollectionDetail.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportFeeYearCollectionDetailInnerServiceSMO;
import com.newland.property.po.reportFee.ReportFeeYearCollectionDetailPo;
import com.newland.property.report.bmo.reportFeeYearCollectionDetail.ISaveReportFeeYearCollectionDetailBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportFeeYearCollectionDetailBMOImpl")
public class SaveReportFeeYearCollectionDetailBMOImpl implements ISaveReportFeeYearCollectionDetailBMO {

    @Autowired
    private IReportFeeYearCollectionDetailInnerServiceSMO reportFeeYearCollectionDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportFeeYearCollectionDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportFeeYearCollectionDetailPo reportFeeYearCollectionDetailPo) {

        reportFeeYearCollectionDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = reportFeeYearCollectionDetailInnerServiceSMOImpl.saveReportFeeYearCollectionDetail(reportFeeYearCollectionDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
