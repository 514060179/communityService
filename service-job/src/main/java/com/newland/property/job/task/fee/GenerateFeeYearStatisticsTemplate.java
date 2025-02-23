package com.newland.property.job.task.fee;

import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.report.IGeneratorFeeYearStatisticsInnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateFeeYearStatisticsTemplate extends TaskSystemQuartz {
    private static final Logger logger = LoggerFactory.getLogger(GenerateFeeYearStatisticsTemplate.class);

    @Autowired
    private IGeneratorFeeYearStatisticsInnerServiceSMO generatorFeeYearStatisticsInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                GenerateFeeYearStatistic(taskDto, communityDto);
            } catch (Exception e) {
                logger.error("生成月报表 失败", e);
            }
        }

    }

    private void GenerateFeeYearStatistic(TaskDto taskDto, CommunityDto communityDto) {
        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        reportFeeMonthStatisticsPo.setCommunityId(communityDto.getCommunityId());
        generatorFeeYearStatisticsInnerServiceSMOImpl.generatorData(reportFeeMonthStatisticsPo);
    }


}
