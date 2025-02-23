package com.newland.property.report.statistics.impl;

import com.newland.property.dto.report.QueryStatisticsDto;
import com.newland.property.dto.report.ReportFloorFeeStatisticsDto;
import com.newland.property.intf.report.IReportFloorFeeStatisticsInnerServiceSMO;
import com.newland.property.report.statistics.IFloorFeeStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorFeeStatisticsImpl implements IFloorFeeStatistics {
    @Autowired
    private IReportFloorFeeStatisticsInnerServiceSMO reportFloorFeeStatisticsInnerServiceSMOImpl;
    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorOweRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorOweRoomCount(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorFeeRoomCount(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorFeeRoomCount(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorReceivedFee(queryStatisticsDto);

    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorPreReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorPreReceivedFee(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorHisOweFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorHisOweFee(queryStatisticsDto);

    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorCurReceivableFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorCurReceivableFee(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorCurReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorCurReceivedFee(queryStatisticsDto);
    }

    @Override
    public List<ReportFloorFeeStatisticsDto> getFloorHisReceivedFee(QueryStatisticsDto queryStatisticsDto) {
        return reportFloorFeeStatisticsInnerServiceSMOImpl.getFloorHisReceivedFee(queryStatisticsDto);
    }
}
