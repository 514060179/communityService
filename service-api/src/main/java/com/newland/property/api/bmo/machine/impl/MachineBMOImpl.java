package com.newland.property.api.bmo.machine.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.machine.IMachineBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.machine.MachineDto;
import com.newland.property.intf.common.IMachineInnerServiceSMO;
import com.newland.property.po.machine.MachinePo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName MachineBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:46
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("machineBMOImpl")
public class MachineBMOImpl extends ApiBaseBMO implements IMachineBMO {
    @Autowired
    private IMachineInnerServiceSMO machineInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteMachine(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MachinePo machinePo = BeanConvertUtil.covertBean(paramInJson, MachinePo.class);
        super.delete(dataFlowContext, machinePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_MACHINE);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addMachine(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessMachine = new JSONObject();
        businessMachine.putAll(paramInJson);
        businessMachine.put("state", "1000");
        businessMachine.put("machineId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineId));
        //计算 应收金额

        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        super.insert(dataFlowContext, machinePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MACHINE);
        paramInJson.put("machineId", businessMachine.getString("machineId"));

    }

    /**
     * 添加设备信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateMachine(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessMachine = new JSONObject();
        businessMachine.putAll(paramInJson);
        businessMachine.put("state", "1000");
        //计算 应收金额
        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        super.update(dataFlowContext, machinePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE);
    }

    /**
     * 添加设备信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateMachineState(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        MachineDto machineDto = new MachineDto();
        machineDto.setCommunityId(paramInJson.getString("communityId"));
        machineDto.setMachineId(paramInJson.getString("machineId"));
        List<MachineDto> machineDtos = machineInnerServiceSMOImpl.queryMachines(machineDto);

        Assert.listOnlyOne(machineDtos, "根据设备编码查询到多条记录，请检查数据");
        JSONObject businessMachine = new JSONObject();
        businessMachine.putAll(BeanConvertUtil.beanCovertMap(machineDtos.get(0)));
        businessMachine.put("state", paramInJson.getString("state"));
        //计算 应收金额
        MachinePo machinePo = BeanConvertUtil.covertBean(businessMachine, MachinePo.class);
        super.update(dataFlowContext, machinePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_MACHINE);

    }
}
