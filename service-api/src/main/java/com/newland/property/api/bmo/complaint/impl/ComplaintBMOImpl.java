package com.newland.property.api.bmo.complaint.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.complaint.IComplaintBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IComplaintUserInnerServiceSMO;
import com.newland.property.intf.store.IComplaintInnerServiceSMO;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.po.complaint.ComplaintPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ComplaintBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:13
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("complaintBMOImpl")
public class ComplaintBMOImpl extends ApiBaseBMO implements IComplaintBMO {


    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    /**
     * 添加投诉建议信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateComplaint(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStoreId(paramInJson.getString("storeId"));
        complaintDto.setCommunityId(paramInJson.getString("communityId"));
        complaintDto.setComplaintId(paramInJson.getString("complaintId"));
        List<ComplaintDto> complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);

        Assert.listOnlyOne(complaintDtos, "存在多条记录，或不存在数据" + complaintDto.getComplaintId());

        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(BeanConvertUtil.beanCovertMap(complaintDtos.get(0)));
        businessComplaint.put("state", "10002");
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(businessComplaint, ComplaintPo.class);
        super.update(dataFlowContext, complaintPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMPLAINT);

    }

    /**
     * 添加小区信息
     * 感谢 QQ号 1464955294 分享解决 删除投诉 时 未删除流程bug
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteComplaint(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(paramInJson, ComplaintPo.class);
        super.delete(dataFlowContext, complaintPo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_COMPLAINT);
       // ComplaintDto complaintDto = new ComplaintDto();
        //complaintDto.setTaskId(complaintPo.getTaskId());
        //complaintUserInnerServiceSMOImpl.deleteTask(complaintDto);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addComplaint(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        paramInJson.put("complaintId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_complaintId));
        paramInJson.put("state", "10001");
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(paramInJson, ComplaintPo.class);
        super.insert(dataFlowContext, complaintPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_COMPLAINT);
    }

    /**
     * 添加投诉建议信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void upComplaint(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStoreId(paramInJson.getString("storeId"));
        complaintDto.setComplaintId(paramInJson.getString("complaintId"));
        List<ComplaintDto> complaintDtos = complaintInnerServiceSMOImpl.queryComplaints(complaintDto);

        Assert.listOnlyOne(complaintDtos, "存在多条记录，或不存在数据" + complaintDto.getComplaintId());


        JSONObject businessComplaint = new JSONObject();
        businessComplaint.putAll(paramInJson);
        businessComplaint.put("state", complaintDtos.get(0).getState());
        businessComplaint.put("roomId", complaintDtos.get(0).getRoomId());
        ComplaintPo complaintPo = BeanConvertUtil.covertBean(paramInJson, ComplaintPo.class);
        super.update(dataFlowContext, complaintPo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_COMPLAINT);

    }
}
