package com.newland.property.report.bmo.reportFeeYearCollection.impl;

import com.newland.property.dto.reportFee.ReportFeeYearCollectionDto;
import com.newland.property.intf.report.IReportFeeYearCollectionInnerServiceSMO;
import com.newland.property.report.bmo.reportFeeYearCollection.IGetReportFeeYearCollectionBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getReportFeeYearCollectionBMOImpl")
public class GetReportFeeYearCollectionBMOImpl implements IGetReportFeeYearCollectionBMO {

    @Autowired
    private IReportFeeYearCollectionInnerServiceSMO reportFeeYearCollectionInnerServiceSMOImpl;

    /**
     * @param reportFeeYearCollectionDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ReportFeeYearCollectionDto reportFeeYearCollectionDto) {

        int count = reportFeeYearCollectionInnerServiceSMOImpl.queryReportFeeYearCollectionsCount(reportFeeYearCollectionDto);

        List<ReportFeeYearCollectionDto> reportFeeYearCollectionDtos = null;
        if (count > 0) {
            reportFeeYearCollectionDtos = reportFeeYearCollectionInnerServiceSMOImpl.queryReportFeeYearCollections(reportFeeYearCollectionDto);
        } else {
            reportFeeYearCollectionDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportFeeYearCollectionDto.getRow()), count, reportFeeYearCollectionDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
