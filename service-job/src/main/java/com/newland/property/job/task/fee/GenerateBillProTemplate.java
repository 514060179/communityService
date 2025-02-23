package com.newland.property.job.task.fee;

import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.fee.IRuleGeneratorPayFeeBillV1InnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import com.newland.property.utils.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 根据 pay_fee_rule 生成 pay_fee 账单
 * 只有小区设置了 按月生成费用 为ON是有效
 *
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GenerateBillProTemplate extends TaskSystemQuartz {

    private static final String TASK_ATTR_VALUE_ONCE_MONTH = "005"; //一次性按月出账


    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Autowired
    private IRuleGeneratorPayFeeBillV1InnerServiceSMO ruleGeneratorPayFeeBillV1InnerServiceSMOImpl;



    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                doRuleCreatePayFeeBill(taskDto, communityDto);
            } catch (Throwable e) {

                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("费用出账失败" + communityDto.getCommunityId(), e);
            }
        }

    }

    /**
     * 根据 pay_fee_rule 产生 账单
     *
     * @param taskDto
     * @param communityDto
     */
    private void doRuleCreatePayFeeBill(TaskDto taskDto, CommunityDto communityDto) {



        ruleGeneratorPayFeeBillV1InnerServiceSMOImpl.covertCommunityPayFee(communityDto.getCommunityId());
    }


}
