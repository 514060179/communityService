package com.newland.property.api.bmo.msg.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.bmo.ApiBaseBMO;
import com.newland.property.api.bmo.msg.IMsgBMO;
import com.newland.property.core.context.DataFlowContext;
import com.newland.property.po.message.MsgReadPo;
import com.newland.property.utils.constant.BusinessTypeConstant;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.stereotype.Service;

/**
 * @ClassName MsgBMOImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 23:00
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
@Service("msgBMOImpl")
public class MsgBMOImpl extends ApiBaseBMO implements IMsgBMO {


    @Override
    public void addReadMsg(JSONObject paramInJson, DataFlowContext context) {

        JSONObject businessMsgRead = new JSONObject();
        businessMsgRead.put("msgReadId", "-1");
        businessMsgRead.putAll(paramInJson);
        MsgReadPo msgReadPo = BeanConvertUtil.covertBean(businessMsgRead, MsgReadPo.class);

        super.insert(context, msgReadPo, BusinessTypeConstant.BUSINESS_TYPE_SAVE_MSG_READ);
    }
}
