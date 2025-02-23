package com.newland.property.job.adapt.hcGov;

import com.newland.property.dto.reportData.ReportDataDto;

public interface IReportReturnDataAdapt {
    void reportReturn(ReportDataDto reportDataDto, String extCommunityId);
}
