package com.newland.property.api.bmo.ownerRepair.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.ownerRepair.IOwnerRepairBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.intf.community.IRepairInnerServiceSMO;
import com.newland.property.intf.community.IRepairUserInnerServiceSMO;
import com.newland.property.dto.repair.RepairDto;
import com.newland.property.dto.repair.RepairUserDto;
import com.newland.property.po.owner.RepairPoolPo;
import com.newland.property.po.owner.RepairUserPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.constant.StateConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OwnerRepairBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:19
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("ownerRepairBMOImpl")
public class OwnerRepairBMOImpl extends ApiBaseBMO implements IOwnerRepairBMO {

    private static Logger logger = LoggerFactory.getLogger(OwnerRepairBMOImpl.class);

    @Autowired
    private IRepairUserInnerServiceSMO repairUserInnerServiceSMOImpl;
    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Override
    public void modifyBusinessRepairUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        RepairUserDto repairUserDto = new RepairUserDto();
        repairUserDto.setRepairId(paramInJson.getString("repairId"));
        repairUserDto.setUserId(paramInJson.getString("staffId"));
        List<RepairUserDto> repairUserDtos = repairUserInnerServiceSMOImpl.queryRepairUsers(repairUserDto);
        Assert.isOne(repairUserDtos, "查询到多条数据，repairId=" + repairUserDto.getRepairId() + " userId = " + repairUserDto.getUserId());
        JSONObject businessObj = new JSONObject();
        businessObj.putAll(BeanConvertUtil.beanCovertMap(repairUserDtos.get(0)));
        businessObj.put("state", paramInJson.getString("state"));
        businessObj.put("context", paramInJson.getString("context"));

        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(businessObj, RepairUserPo.class);
        super.update(dataFlowContext, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR_USER);
    }

    @Override
    public void modifyBusinessRepair(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));

        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);

        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());

        logger.debug("查询报修单结果：" + JSONObject.toJSONString(repairDtos.get(0)));

        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", "10002".equals(paramInJson.getString("state")) ? StateConstant.REPAIR_DISPATCH_FINISH : StateConstant.REPAIR_NO_DISPATCH);

        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        super.update(dataFlowContext, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(paramInJson);

        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        super.delete(dataFlowContext, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_REPAIR);
    }


    @Override
    public void addBusinessRepairUser(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessObj = new JSONObject();
        businessObj.putAll(paramInJson);
        businessObj.put("state", StateConstant.STAFF_NO_FINISH_ORDER);
        businessObj.put("ruId", "-1");
        RepairUserPo repairUserPo = BeanConvertUtil.covertBean(businessObj, RepairUserPo.class);
        repairUserPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        super.insert(dataFlowContext, repairUserPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR_USER);
    }

    @Override
    public void modifyBusinessRepairDispatch(JSONObject paramInJson, DataFlowContext dataFlowContext, String state) {
        //查询报修单
        RepairDto repairDto = new RepairDto();
        repairDto.setRepairId(paramInJson.getString("repairId"));
        List<RepairDto> repairDtos = repairInnerServiceSMOImpl.queryRepairs(repairDto);
        Assert.isOne(repairDtos, "查询到多条数据，repairId=" + repairDto.getRepairId());
        logger.debug("查询报修单结果：" + JSONObject.toJSONString(repairDtos.get(0)));
        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(BeanConvertUtil.beanCovertMap(repairDtos.get(0)));
        businessOwnerRepair.put("state", state);
        //计算 应收金额
        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        super.update(dataFlowContext, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(paramInJson);
        businessOwnerRepair.put("repairId", "-1");
        businessOwnerRepair.put("state", StateConstant.REPAIR_NO_DISPATCH);

        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        super.insert(dataFlowContext, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_REPAIR);
    }

    /**
     * 添加业主报修信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateOwnerRepair(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        JSONObject businessOwnerRepair = new JSONObject();
        businessOwnerRepair.putAll(paramInJson);

        RepairPoolPo repairPoolPo = BeanConvertUtil.covertBean(businessOwnerRepair, RepairPoolPo.class);
        super.update(dataFlowContext, repairPoolPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_REPAIR);
    }

}
