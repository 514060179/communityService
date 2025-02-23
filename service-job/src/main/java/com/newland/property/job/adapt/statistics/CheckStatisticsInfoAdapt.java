package com.newland.property.job.adapt.statistics;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.store.ISmallWeChatInnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.newland.property.intf.user.IOwnerAppUserInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 数据统计适配器
 *
 * @author fqz
 * @date 2020-12-11  18:54
 */
@Component(value = "checkStatisticsInfoAdapt")
public class CheckStatisticsInfoAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(CheckStatisticsInfoAdapt.class);

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;


    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMO;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMO;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    public final static String ALI_SMS_DOMAIN = "ALI_SMS";

    @Override
    public void execute(Business business, List<Business> businesses) {
        
    }

    private void doSendPayFeeDetail(Business business, JSONObject businessPayFeeDetail) {

    }
}
