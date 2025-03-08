/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.store.cmd.scheduleClasses;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.classes.ClassesTimeDto;
import com.newland.property.dto.classes.ScheduleClassesDayDto;
import com.newland.property.dto.classes.ScheduleClassesTimeDto;
import com.newland.property.intf.store.IClassesTimeV1InnerServiceSMO;
import com.newland.property.intf.store.IScheduleClassesDayV1InnerServiceSMO;
import com.newland.property.intf.store.IScheduleClassesTimeV1InnerServiceSMO;
import com.newland.property.po.classes.ScheduleClassesDayPo;
import com.newland.property.po.classes.ScheduleClassesTimePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：scheduleClasses.updateScheduleClassesDay
 * 请求路劲：/app/scheduleClasses.updateScheduleClassesDay
 *
 */
@NewlandPropertyCmd(serviceCode = "scheduleClasses.updateScheduleClassesDay")
public class UpdateScheduleClassesDayCmd extends Cmd {

  private static Logger logger = LoggerFactory.getLogger(UpdateScheduleClassesDayCmd.class);


    @Autowired
    private IClassesTimeV1InnerServiceSMO classesTimeV1InnerServiceSMOImpl;


    @Autowired
    private IScheduleClassesDayV1InnerServiceSMO scheduleClassesDayV1InnerServiceSMOImpl;

    @Autowired
    private IScheduleClassesTimeV1InnerServiceSMO scheduleClassesTimeV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "staffId", "staffId不能为空");
        Assert.hasKeyAndValue(reqJson, "scheduleId", "scheduleId不能为空");
        Assert.hasKeyAndValue(reqJson, "curDate", "curDate不能为空");
        Assert.hasKeyAndValue(reqJson, "classId", "classId不能为空");
        Assert.hasKeyAndValue(reqJson, "day", "day不能为空");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        /**
         * 1、新增或者修改新表(schedule_classes_day_self表)
         * 2、新增时间表数据 schedule_classes_time
         */
        String day = reqJson.getString("day");
        String staffId = reqJson.getString("staffId");
        String curDate = reqJson.getString("curDate");
        String classId = reqJson.getString("classId");
        String scheduleId = reqJson.getString("scheduleId");

        ScheduleClassesDayDto scheduleClassesDayDto = new ScheduleClassesDayDto();
        scheduleClassesDayDto.setDay(day);
        scheduleClassesDayDto.setScheduleId(scheduleId);
        scheduleClassesDayDto.setStatusCd("0");
        scheduleClassesDayDto.setScheduleDate(curDate);
        List<ScheduleClassesDayDto> scheduleClassesDayDtos = scheduleClassesDayV1InnerServiceSMOImpl.queryScheduleClassesDaySelf(scheduleClassesDayDto);
        ClassesTimeDto classesTimeDto = new ClassesTimeDto();
        classesTimeDto.setClassesId(classId);
        classesTimeDto.setStatusCd("0");
        List<ClassesTimeDto> classesTimeDtos = classesTimeV1InnerServiceSMOImpl.queryClassesTimes(classesTimeDto);
        if (classesTimeDtos == null || classesTimeDtos.size() < 1) {
            logger.error("该班次{}不存在时间表",classId);
            throw new CmdException("该班次"+classId+"不存在时间表");
        }
        String newDayId = null;
        if (scheduleClassesDayDtos == null || scheduleClassesDayDtos.size() < 1) {
            logger.info("员工不存在该排班scheduleId={},day={}不存在，需要获取新增个人排班信息",scheduleId,day);
            // 2、新增数据
            ScheduleClassesDayPo saveScheduleClassesDayPo = new ScheduleClassesDayPo();
            newDayId = GenerateCodeFactory.getGeneratorId("11");
            saveScheduleClassesDayPo.setWorkday(classId);
            saveScheduleClassesDayPo.setDayId(newDayId);
            saveScheduleClassesDayPo.setStatusCd("0");
            saveScheduleClassesDayPo.setDay(day);
            saveScheduleClassesDayPo.setScheduleId(scheduleId);
            saveScheduleClassesDayPo.setWeekFlag("1");
            saveScheduleClassesDayPo.setStaffId(staffId);
            saveScheduleClassesDayPo.setScheduleDate(curDate);
            scheduleClassesDayV1InnerServiceSMOImpl.saveScheduleClassesDaySelf(saveScheduleClassesDayPo);
        }else{
            ScheduleClassesDayDto queryScheduleClassesDayDto = scheduleClassesDayDtos.get(0);
            newDayId = queryScheduleClassesDayDto.getDayId();
            // 2、修改个人排班数据
            ScheduleClassesDayPo updScheduleClassesDayPo = new ScheduleClassesDayPo();
            updScheduleClassesDayPo.setWorkday(classId);
            updScheduleClassesDayPo.setStatusCd("0");
            updScheduleClassesDayPo.setDay(day);
            updScheduleClassesDayPo.setScheduleId(scheduleId);
//            updScheduleClassesDayPo.setWeekFlag("1");
//            updScheduleClassesDayPo.setStaffId(staffId);
            updScheduleClassesDayPo.setScheduleDate(curDate);
            scheduleClassesDayV1InnerServiceSMOImpl.updateScheduleClassesDaySelf(updScheduleClassesDayPo);
            // 修改排班时间
            ScheduleClassesTimePo scheduleClassesTimePo = new ScheduleClassesTimePo();
            scheduleClassesTimePo.setStatusCd("1");
            scheduleClassesTimePo.setDayId(newDayId);
            scheduleClassesTimeV1InnerServiceSMOImpl.updateScheduleClassesTime(scheduleClassesTimePo);
        }
        // 3.1 新增时间表数据 schedule_classes_time
        for (int i = 0; i < classesTimeDtos.size(); i++) {
            ScheduleClassesTimePo scheduleClassesTimePo = new ScheduleClassesTimePo();
            scheduleClassesTimePo.setTimeId(GenerateCodeFactory.getGeneratorId("11"));
            scheduleClassesTimePo.setDayId(newDayId);
            scheduleClassesTimePo.setStartTime(classesTimeDtos.get(i).getStartTime());
            scheduleClassesTimePo.setStatusCd(classesTimeDtos.get(i).getStatusCd());
            scheduleClassesTimePo.setEndTime(classesTimeDtos.get(i).getEndTime());
            scheduleClassesTimePo.setScheduleId(scheduleId);
            scheduleClassesTimeV1InnerServiceSMOImpl.saveScheduleClassesTime(scheduleClassesTimePo);
        }
        logger.info("修改排班成功");
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
