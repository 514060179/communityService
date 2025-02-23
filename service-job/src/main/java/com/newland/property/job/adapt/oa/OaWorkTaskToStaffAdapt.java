package com.newland.property.job.adapt.oa;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.system.Business;
import com.newland.property.dto.workPool.WorkPoolDto;
import com.newland.property.dto.workTask.WorkTaskDto;
import com.newland.property.dto.workType.WorkTypeDto;
import com.newland.property.intf.oa.IWorkPoolV1InnerServiceSMO;
import com.newland.property.intf.oa.IWorkTaskV1InnerServiceSMO;
import com.newland.property.intf.oa.IWorkTypeV1InnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.msgNotify.IMsgNotify;
import com.newland.property.job.msgNotify.MsgNotifyFactory;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.ListUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.List;

@Component("oaWorkTaskToStaffAdapt")
public class OaWorkTaskToStaffAdapt extends DatabusAdaptImpl {
    private static Logger logger = LoggerFactory.getLogger(OaWorkTaskToStaffAdapt.class);

    @Autowired
    private IWorkTaskV1InnerServiceSMO workTaskV1InnerServiceSMOImpl;

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkTypeV1InnerServiceSMO workTypeV1InnerServiceSMOImpl;

    @Override
    public void execute(Business business, List<Business> businesses) throws ParseException {
        JSONObject data = business.getData();

        Assert.hasKeyAndValue(data, "taskId", "未包含任务ID");

        WorkTaskDto workTaskDto = new WorkTaskDto();
        workTaskDto.setTaskId(data.getString("taskId"));
        List<WorkTaskDto> workTaskDtos = workTaskV1InnerServiceSMOImpl.queryWorkTasks(workTaskDto);

        if (ListUtil.isNull(workTaskDtos)) {
            return;
        }
        workTaskDto = workTaskDtos.get(0);

        WorkPoolDto workPoolDto = new WorkPoolDto();
        workPoolDto.setWorkId(workTaskDto.getWorkId());
        List<WorkPoolDto> workPoolDtos = workPoolV1InnerServiceSMOImpl.queryWorkPools(workPoolDto);

        if (ListUtil.isNull(workPoolDtos)) {
            return;
        }

        WorkTypeDto workTypeDto = new WorkTypeDto();
        workTypeDto.setWtId(workPoolDtos.get(0).getWtId());
        List<WorkTypeDto> workTypeDtos = workTypeV1InnerServiceSMOImpl.queryWorkTypes(workTypeDto);
        if (ListUtil.isNull(workTypeDtos)) {
            return;
        }

        if (WorkTaskDto.STATE_WAIT.equals(workTaskDto)) {
            todoStaff(workTaskDto, workPoolDtos.get(0), workTypeDtos.get(0));
        } else if (WorkTaskDto.STATE_COMPLETE.equals(workTaskDto)) {
            todoStartStaff(workTaskDto, workPoolDtos.get(0), workTypeDtos.get(0));
        }
    }

    /**
     * 通知
     *
     * @param workTaskDto
     * @param workPoolDto
     * @param workTypeDto
     */
    private void todoStartStaff(WorkTaskDto workTaskDto, WorkPoolDto workPoolDto, WorkTypeDto workTypeDto) {

        //todo 给申请人发消息
        JSONObject content = new JSONObject();
        content.put("flowName", workPoolDto.getWorkName());
        content.put("staffName", "处理完成("+workTaskDto.getStaffName()+")");
        content.put("orderId", workPoolDto.getWorkId());
        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);

        if(WorkTypeDto.SMS_WAY_WECHAT.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
            msgNotify.sendOaCreateStaffMsg(workTypeDto.getCommunityId(), workTaskDto.getCreateUserId(), content);
        }else if(WorkTypeDto.SMS_WAY_ALI.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
            msgNotify.sendOaCreateStaffMsg(workTypeDto.getCommunityId(), workTaskDto.getCreateUserId(), content);
        }
    }

    /**
     * 派单或转单
     *
     * @param workTaskDto
     * @param workPoolDto
     * @param workTypeDto
     */
    private void todoStaff(WorkTaskDto workTaskDto, WorkPoolDto workPoolDto, WorkTypeDto workTypeDto) {

        JSONObject content = new JSONObject();
        content.put("flowName", workPoolDto.getWorkName());
        content.put("create_user_name", workTaskDto.getStaffName());
        content.put("create_time", workTaskDto.getCreateTime());
        content.put("date", DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B));
        content.put("orderId", workPoolDto.getWorkId());

        String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN, "STAFF_WECHAT_URL");
        content.put("url", wechatUrl);
        if(WorkTypeDto.SMS_WAY_WECHAT.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_WECHAT);
            msgNotify.sendOaDistributeMsg(workTypeDto.getCommunityId(), workTaskDto.getStaffId(), content);
        }else if(WorkTypeDto.SMS_WAY_ALI.equals(workTypeDto.getSmsWay())){
            IMsgNotify msgNotify = MsgNotifyFactory.getMsgNotify(MsgNotifyFactory.NOTIFY_WAY_ALI);
            msgNotify.sendOaDistributeMsg(workTypeDto.getCommunityId(), workTaskDto.getStaffId(), content);
        }
    }
}
