package com.newland.property.intf.job;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.data.DatabusDataDto;
import com.newland.property.dto.machine.CarInoutDto;
import com.newland.property.dto.machine.MachineDto;
import com.newland.property.dto.fee.TempCarPayOrderDto;
import com.newland.property.dto.system.Business;
import com.newland.property.vo.ResultVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName ITaskInnerServiceSMO
 * @Description dataBus统一处理类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/dataBusApi")
public interface IDataBusInnerServiceSMO {

    /**
     * <p>查询小区楼信息</p>
     *
     * @param businesses 业务
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/exchange", method = RequestMethod.POST)
    boolean exchange(@RequestBody List<Business> businesses);

    /**
     * 自定义databus 数据 传输
     * @param databusDataDto
     * @return
     */
    @RequestMapping(value = "/databusData", method = RequestMethod.POST)
    boolean databusData(@RequestBody DatabusDataDto databusDataDto);

    /**
     * <p>开门</p>
     *
     * @param reqJson 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/openDoor", method = RequestMethod.POST)
    ResultVo openDoor(@RequestBody JSONObject reqJson);
    /**
     * <p>开门</p>
     *
     * @param reqJson 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/closeDoor", method = RequestMethod.POST)
    ResultVo closeDoor(@RequestBody JSONObject reqJson);

    /**
     * <p>重启设备</p>
     *
     * @param reqJson 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/restartMachine", method = RequestMethod.POST)
    ResultVo restartMachine(@RequestBody JSONObject reqJson);

    /**
     * <p>重启设备</p>
     *
     * @param reqJson 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/resendIot", method = RequestMethod.POST)
    ResultVo resendIot(@RequestBody JSONObject reqJson);

    /**
     * <p>查询待支付订单</p>
     *
     * @param tempCarPayOrderDto 请求信息
     * @return TaskDto 对象数据
     */
    @RequestMapping(value = "/getTempCarFeeOrder", method = RequestMethod.POST)
    ResultVo getTempCarFeeOrder(@RequestBody TempCarPayOrderDto tempCarPayOrderDto);

    @RequestMapping(value = "/notifyTempCarFeeOrder", method = RequestMethod.POST)
    ResultVo notifyTempCarFeeOrder(@RequestBody TempCarPayOrderDto tempCarPayOrderDto);




    @RequestMapping(value = "/getQRcode", method = RequestMethod.POST)
    ResultVo getQRcode(@RequestBody JSONObject reqJson);

    @RequestMapping(value = "/customCarInOut", method = RequestMethod.POST)
    ResultVo customCarInOut(@RequestBody JSONObject reqJson);

    @RequestMapping(value = "/payVideo", method = RequestMethod.POST)
    ResultVo payVideo(@RequestBody MachineDto machineDto);

    @RequestMapping(value = "/heartbeatVideo", method = RequestMethod.POST)
    ResultVo heartbeatVideo(@RequestBody JSONObject reqJson);
    @RequestMapping(value = "/updateCarInoutCarNum", method = RequestMethod.POST)
    ResultVo updateCarInoutCarNum(@RequestBody CarInoutDto carInoutDto);

    @RequestMapping(value = "/getManualOpenDoorLogs", method = RequestMethod.POST)
    ResultVo getManualOpenDoorLogs(@RequestBody JSONObject reqJson);

    @RequestMapping(value = "/tempCarAuth", method = RequestMethod.POST)
    ResultVo tempCarAuth(@RequestBody JSONObject reqJson);

    @RequestMapping(value = "/getTempCarAuths", method = RequestMethod.POST)
    ResultVo getTempCarAuths(@RequestBody JSONObject reqJson);
}
