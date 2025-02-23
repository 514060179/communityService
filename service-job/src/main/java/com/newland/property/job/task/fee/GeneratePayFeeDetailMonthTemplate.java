package com.newland.property.job.task.fee;

import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.dto.payFee.PayFeeDetailRefreshFeeMonthDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.fee.IPayFeeMonthInnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import com.newland.property.utils.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName GeneratePayFeeDetailMonthTemplate
 * @Description TODO  费用离散处理任务
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class GeneratePayFeeDetailMonthTemplate extends TaskSystemQuartz {

    @Autowired
    private IPayFeeMonthInnerServiceSMO payFeeMonthInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            try {
                generateMonthFee(taskDto, communityDto);
            } catch (Exception e) {
                e.printStackTrace();
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
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void generateMonthFee(TaskDto taskDto, CommunityDto communityDto) {

        PayFeeDetailRefreshFeeMonthDto payFeeDetailRefreshFeeMonthDto = new PayFeeDetailRefreshFeeMonthDto();
        payFeeDetailRefreshFeeMonthDto.setCommunityId(communityDto.getCommunityId());
        payFeeMonthInnerServiceSMOImpl.doGeneratorOrRefreshAllFeeMonth(payFeeDetailRefreshFeeMonthDto);

    }


}
