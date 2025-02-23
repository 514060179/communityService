package com.newland.property.job.task.fee;

import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.report.IGeneratorOweFeeInnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.po.reportFee.ReportFeeMonthStatisticsPo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  房屋费用账单生成
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateOweFeeNewTemplate extends TaskSystemQuartz {
    private static final Logger logger = LoggerFactory.getLogger(GenerateOweFeeNewTemplate.class);

    @Autowired
    private IGeneratorOweFeeInnerServiceSMO generatorOweFeeInnerServiceSMOImpl;


    @Override
    protected void process(TaskDto taskDto) throws Exception {
        GenerateFeeYearStatistic(taskDto);
    }

    private void GenerateFeeYearStatistic(TaskDto taskDto) {
        ReportFeeMonthStatisticsPo reportFeeMonthStatisticsPo = new ReportFeeMonthStatisticsPo();
        generatorOweFeeInnerServiceSMOImpl.generatorOweData(reportFeeMonthStatisticsPo);
    }


}
