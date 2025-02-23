package com.newland.property.common.smartMeter;

import com.newland.property.dto.meter.MeterMachineDto;
import com.newland.property.dto.meter.MeterMachineDetailDto;

/**
 * 水电抄表逻辑
 */
public interface ISmartMeterCoreRead {

    /**
     * 抄表
     */
    void saveMeterAndCreateFee(MeterMachineDetailDto meterMachineDetailDto, String degree,String batchId);

    String generatorBatch(String communityId);

    /**
     * 查询 表读数
     * @param meterMachineDto
     * @return
     */
    double getMeterDegree(MeterMachineDto meterMachineDto);
}
