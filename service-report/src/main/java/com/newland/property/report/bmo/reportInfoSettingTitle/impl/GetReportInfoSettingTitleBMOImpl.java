package com.newland.property.report.bmo.reportInfoSettingTitle.impl;

import com.newland.property.dto.reportInfo.ReportInfoSettingTitleValueDto;
import com.newland.property.intf.report.IReportInfoSettingTitleInnerServiceSMO;
import com.newland.property.intf.report.IReportInfoSettingTitleValueInnerServiceSMO;
import com.newland.property.report.bmo.reportInfoSettingTitle.IGetReportInfoSettingTitleBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.newland.property.dto.reportInfo.ReportInfoSettingTitleDto;

import java.util.ArrayList;
import java.util.List;

@Service("getReportInfoSettingTitleBMOImpl")
public class GetReportInfoSettingTitleBMOImpl implements IGetReportInfoSettingTitleBMO {

    @Autowired
    private IReportInfoSettingTitleInnerServiceSMO reportInfoSettingTitleInnerServiceSMOImpl;
    @Autowired
    private IReportInfoSettingTitleValueInnerServiceSMO reportInfoSettingTitleValueInnerServiceSMOImpl;
    /**
     *
     *
     * @param  reportInfoSettingTitleDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ReportInfoSettingTitleDto reportInfoSettingTitleDto) {


        int count = reportInfoSettingTitleInnerServiceSMOImpl.queryReportInfoSettingTitlesCount(reportInfoSettingTitleDto);

        List<ReportInfoSettingTitleDto> reportInfoSettingTitleDtos = null;
        if (count > 0) {
            reportInfoSettingTitleDtos = reportInfoSettingTitleInnerServiceSMOImpl.queryReportInfoSettingTitles(reportInfoSettingTitleDto);
            refreshTitileValues(reportInfoSettingTitleDtos);
        } else {
            reportInfoSettingTitleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reportInfoSettingTitleDto.getRow()), count, reportInfoSettingTitleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }


    private void refreshTitileValues(List<ReportInfoSettingTitleDto> reportInfoSettingTitleDtos) {

        if (reportInfoSettingTitleDtos == null || reportInfoSettingTitleDtos.size() < 1) {
            return;
        }

        List<String> titleIds = new ArrayList<>();
        for (ReportInfoSettingTitleDto reportInfoSettingTitleDto : reportInfoSettingTitleDtos) {
            titleIds.add(reportInfoSettingTitleDto.getTitleId());
        }

        ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto = new ReportInfoSettingTitleValueDto();
        reportInfoSettingTitleValueDto.setTitleIds(titleIds.toArray(new String[titleIds.size()]));
        reportInfoSettingTitleValueDto.setCommunityId(reportInfoSettingTitleDtos.get(0).getCommunityId());
        List<ReportInfoSettingTitleValueDto> reportInfoSettingTitleValueDtos
                = reportInfoSettingTitleValueInnerServiceSMOImpl.queryReportInfoSettingTitleValues(reportInfoSettingTitleValueDto);

        List<ReportInfoSettingTitleValueDto> tmpReportInfoSettingTitleValueDtos = null;
        for (ReportInfoSettingTitleDto reportInfoSettingTitleDto : reportInfoSettingTitleDtos) {
            tmpReportInfoSettingTitleValueDtos = new ArrayList<>();
            for (ReportInfoSettingTitleValueDto reportInfoSettingTitleValueDto1 : reportInfoSettingTitleValueDtos) {
                if (reportInfoSettingTitleDto.getTitleId().equals(reportInfoSettingTitleValueDto1.getTitleId())) {
                    tmpReportInfoSettingTitleValueDtos.add(reportInfoSettingTitleValueDto1);
                }
            }
            reportInfoSettingTitleDto.setReportInfoSettingTitleValueDtos(tmpReportInfoSettingTitleValueDtos);
        }


    }
}
