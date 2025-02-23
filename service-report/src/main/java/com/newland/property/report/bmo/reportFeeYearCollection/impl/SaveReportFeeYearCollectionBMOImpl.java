package com.newland.property.report.bmo.reportFeeYearCollection.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.newland.property.po.reportFee.ReportFeeYearCollectionPo;
import com.newland.property.report.bmo.reportFeeYearCollection.ISaveReportFeeYearCollectionBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveReportFeeYearCollectionBMOImpl")
public class SaveReportFeeYearCollectionBMOImpl implements ISaveReportFeeYearCollectionBMO {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param reportFeeYearCollectionPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(ReportFeeYearCollectionPo reportFeeYearCollectionPo) {

        reportFeeYearCollectionPo.setCollectionId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_collectionId));
        int flag = reportFeeYearCollectionInnerServiceSMOImpl.saveReportFeeYearCollection(reportFeeYearCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
