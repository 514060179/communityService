package com.newland.property.common.charge;

import com.newland.property.dto.charge.ChargeMachineDto;
import com.newland.property.dto.charge.NotifyChargeOrderDto;
import com.newland.property.dto.charge.NotifyChargePortDto;
import com.newland.property.dto.charge.ChargeMachinePortDto;
import com.newland.property.vo.ResultVo;

import java.util.List;

/**
 * 充电厂家适配器
 */
public interface IChargeFactoryAdapt {

    /**
     * 开始充电
     *
     * @param chargeMachineDto
     * @param chargeMachinePortDto
     * @param chargeType
     * @param duration
     * @return
     */
    ResultVo startCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto, String chargeType, double duration,String orderId);

    /**
     * 结束充电
     *
     * @param chargeMachineDto
     * @param chargeMachinePortDto
     * @return
     */
    ResultVo stopCharge(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto);

    /**
     * 查询充电桩状态
     * @param chargeMachineDto
     * @param chargeMachinePortDto
     * @return
     */
    ChargeMachinePortDto getChargePortState(ChargeMachineDto chargeMachineDto, ChargeMachinePortDto chargeMachinePortDto);

    /**
     * 解析充电心跳参数
     * @param notifyChargeOrderDto
     * @return
     */
    List<NotifyChargePortDto> getChargeHeartBeatParam(NotifyChargeOrderDto notifyChargeOrderDto);

    /**
     * 查询设备状态
     * @param chargeMachineDto
     */
    void queryChargeMachineState(ChargeMachineDto chargeMachineDto);

    /**
     * 工作心跳
     * @param chargeMachineDto
     * @param bodyParam
     */
    void workHeartbeat(ChargeMachineDto chargeMachineDto, String bodyParam);
}
