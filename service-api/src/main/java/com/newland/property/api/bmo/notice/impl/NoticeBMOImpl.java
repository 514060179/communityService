package com.newland.property.api.bmo.notice.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.notice.INoticeBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.dto.notice.NoticeDto;
import com.newland.property.po.notice.NoticePo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName NoticeBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:02
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("noticeBMOImpl")
public class NoticeBMOImpl extends ApiBaseBMO implements INoticeBMO {

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void deleteNotice(JSONObject paramInJson, DataFlowContext dataFlowContext) {
        JSONObject businessNotice = new JSONObject();
        businessNotice.putAll(paramInJson);
        NoticePo noticePo = BeanConvertUtil.covertBean(paramInJson, NoticePo.class);
        super.delete(dataFlowContext, noticePo, BusinessTypeConstant.BUSINESS_TYPE_DELETE_NOTICE);
    }

    /**
     * 添加小区信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void addNotice(JSONObject paramInJson, DataFlowContext dataFlowContext) {

        JSONObject businessNotice = new JSONObject();
        businessNotice.putAll(paramInJson);
        businessNotice.put("noticeId", "-1");
        if (!paramInJson.containsKey("state")) {
            businessNotice.put("state", NoticeDto.STATE_FINISH);
        }
        NoticePo noticePo = BeanConvertUtil.covertBean(businessNotice, NoticePo.class);
        super.insert(dataFlowContext, noticePo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_NOTICE);
    }

    /**
     * 添加公告信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    @Override
    public void updateNotice(JSONObject paramInJson, DataFlowContext dataFlowContext) {


        NoticePo noticePo = BeanConvertUtil.covertBean(paramInJson, NoticePo.class);
        super.update(dataFlowContext, noticePo, BusinessTypeConstant.BUSINESS_TYPE_UPDATE_NOTICE);
    }
}
