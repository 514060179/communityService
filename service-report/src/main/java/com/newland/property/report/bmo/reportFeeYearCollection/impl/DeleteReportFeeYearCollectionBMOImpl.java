package com.newland.property.report.bmo.reportFeeYearCollection.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.newland.property.po.reportFee.ReportFeeYearCollectionPo;
import com.newland.property.report.bmo.reportFeeYearCollection.IDeleteReportFeeYearCollectionBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteReportFeeYearCollectionBMOImpl")
public class DeleteReportFeeYearCollectionBMOImpl implements IDeleteReportFeeYearCollectionBMO {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ReportFeeYearCollectionPo reportFeeYearCollectionPo) {

        int flag = reportFeeYearCollectionInnerServiceSMOImpl.deleteReportFeeYearCollection(reportFeeYearCollectionPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
