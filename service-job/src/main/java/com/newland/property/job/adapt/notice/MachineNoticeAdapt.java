package com.newland.property.job.adapt.notice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.factory.WechatFactory;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.notice.NoticeDto;
import com.newland.property.dto.owner.OwnerAppUserDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.wechat.SmallWeChatDto;
import com.newland.property.dto.wechat.SmallWechatAttrDto;
import com.newland.property.dto.user.StaffAppAuthDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.system.Business;
import com.newland.property.dto.wechat.Content;
import com.newland.property.dto.wechat.Data;
import com.newland.property.dto.wechat.PropertyFeeTemplateMessage;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.community.INoticeInnerServiceSMO;
import com.newland.property.intf.store.ISmallWeChatInnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.newland.property.intf.user.IOwnerAppUserInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IStaffAppAuthInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.po.notice.NoticePo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.cache.UrlCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 发布公告推送通知
 *
 * @author fqz
 * @date 2022-10-15
 */
@Component(value = "machineNoticeAdapt")
public class MachineNoticeAdapt extends DatabusAdaptImpl {

    private static Logger logger = LoggerFactory.getLogger(MachineNoticeAdapt.class);

    @Autowired
    private INoticeInnerServiceSMO noticeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerAppUserInnerServiceSMO ownerAppUserInnerServiceSMOImpl;

    @Autowired
    private RestTemplate outRestTemplate;

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray businessNotices = new JSONArray();
        System.out.println("收到日志：>>>>>>>>>>>>>" + data.toJSONString());
        if (data.containsKey(NoticePo.class.getSimpleName())) {
            Object bObj = data.get(NoticePo.class.getSimpleName());
            if (bObj instanceof JSONObject) {
                businessNotices.add(bObj);
            } else if (bObj instanceof List) {
                businessNotices = JSONArray.parseArray(JSONObject.toJSONString(bObj));
            } else {
                businessNotices = (JSONArray) bObj;
            }
        } else {
            if (data instanceof JSONObject) {
                businessNotices.add(data);
            }
        }
        for (int noticeIndex = 0; noticeIndex < businessNotices.size(); noticeIndex++) {
            JSONObject businessNotice = businessNotices.getJSONObject(noticeIndex);
            doDealNotice(businesses, businessNotice);
        }
    }

    private void doDealNotice(List<Business> businesses, JSONObject businessNotice) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setNoticeId(businessNotice.getString("noticeId"));
        List<NoticeDto> noticeDtos = noticeInnerServiceSMOImpl.queryNotices(noticeDto);
        Assert.listOnlyOne(noticeDtos, "查询公告错误！");
        //获取标题
        String title = businessNotice.getString("title");
        //获取开始时间
        String startTime = businessNotice.getString("startTime");
        //获取备注内容
        String context = businessNotice.getString("context");
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(businessNotice.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMOImpl.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "查询小区错误！");
        if (!StringUtil.isEmpty(businessNotice.getString("noticeTypeCd")) && "1001".equals(businessNotice.getString("noticeTypeCd"))) { //员工通知
            JSONObject paramIn = new JSONObject();
            paramIn.put("title", title);
            paramIn.put("startTime", startTime);
            paramIn.put("context", context);
            //给员工推送通知
            sendMsg(paramIn, communityDtos.get(0));
        } else if (!StringUtil.isEmpty(businessNotice.getString("noticeTypeCd")) && "1000".equals(businessNotice.getString("noticeTypeCd"))) { //业主通知
            JSONObject paramIn = new JSONObject();
            paramIn.put("title", title);
            paramIn.put("startTime", startTime);
            paramIn.put("context", context);
            //给业主推送通知
            publishMsg(paramIn, communityDtos.get(0));
        } else if (!StringUtil.isEmpty(businessNotice.getString("noticeTypeCd")) && "1002".equals(businessNotice.getString("noticeTypeCd"))) { //小区通知
            JSONObject paramIn = new JSONObject();
            paramIn.put("title", title);
            paramIn.put("startTime", startTime);
            paramIn.put("context", context);
            //给业主推送通知
            publishMsg(paramIn, communityDtos.get(0));
        }
    }

    /**
     * 给员工推送通知
     *
     * @param paramIn
     * @param communityDto
     */
    private void sendMsg(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        UserDto userDto = new UserDto();
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        for (UserDto user : users) {
            StaffAppAuthDto staffAppAuthDto = new StaffAppAuthDto();
            staffAppAuthDto.setStaffId(user.getUserId());
            staffAppAuthDto.setAppType("WECHAT");
            List<StaffAppAuthDto> staffAppAuthDtos = staffAppAuthInnerServiceSMOImpl.queryStaffAppAuths(staffAppAuthDto);
            if (staffAppAuthDtos != null && staffAppAuthDtos.size() > 0) {
                String openId = staffAppAuthDtos.get(0).getOpenId();
                String url = sendMsgUrl + accessToken;
                Data data = new Data();
                PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                templateMessage.setTemplate_id(templateId);
                templateMessage.setTouser(openId);
                data.setFirst(new Content(paramIn.getString("title")));
                data.setKeyword1(new Content(paramIn.getString("title")));
                data.setKeyword2(new Content(paramIn.getString("startTime")));
                data.setKeyword3(new Content(StringUtil.delHtmlTag(paramIn.getString("context"))));
                data.setRemark(new Content("如有疑问请联系相关物业人员"));
                templateMessage.setData(BeanConvertUtil.beanCovertJson(data));
                //获取员工公众号地址
                String wechatUrl = MappingCache.getValue(MappingConstant.URL_DOMAIN,"STAFF_WECHAT_URL");
                templateMessage.setUrl(wechatUrl);
                logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                logger.info("微信模板返回内容:{}", responseEntity);
            }
        }
    }

    /**
     * 给业主推送通知
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishMsg(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }
        SmallWeChatDto weChatDto = smallWeChatDtos.get(0);
        SmallWechatAttrDto smallWechatAttrDto = new SmallWechatAttrDto();
        smallWechatAttrDto.setCommunityId(communityDto.getCommunityId());
        smallWechatAttrDto.setWechatId(weChatDto.getWeChatId());
        smallWechatAttrDto.setSpecCd(SmallWechatAttrDto.SPEC_CD_WECHAT_TEMPLATE);
        List<SmallWechatAttrDto> smallWechatAttrDtos = smallWechatAttrInnerServiceSMOImpl.querySmallWechatAttrs(smallWechatAttrDto);
        if (smallWechatAttrDtos == null || smallWechatAttrDtos.size() <= 0) {
            logger.info("未配置微信公众号消息模板");
            return;
        }
        String templateId = smallWechatAttrDtos.get(0).getValue();
        String accessToken = WechatFactory.getAccessToken(weChatDto.getAppId(), weChatDto.getAppSecret());
        if (StringUtil.isEmpty(accessToken)) {
            logger.info("推送微信模板,获取accessToken失败:{}", accessToken);
            return;
        }
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setCommunityId(communityDto.getCommunityId());
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        for (OwnerDto owner : ownerDtos) {
            OwnerAppUserDto ownerAppUserDto = new OwnerAppUserDto();
            ownerAppUserDto.setMemberId(owner.getMemberId());
            ownerAppUserDto.setAppType("WECHAT");
            //查询绑定业主
            List<OwnerAppUserDto> ownerAppUserDtos = ownerAppUserInnerServiceSMOImpl.queryOwnerAppUsers(ownerAppUserDto);
            if (ownerAppUserDtos.size() > 0) {
                //获取openId
                String openId = ownerAppUserDtos.get(0).getOpenId();
                String url = sendMsgUrl + accessToken;
                Data data = new Data();
                PropertyFeeTemplateMessage templateMessage = new PropertyFeeTemplateMessage();
                templateMessage.setTemplate_id(templateId);
                templateMessage.setTouser(openId);
                data.setFirst(new Content(paramIn.getString("title")));
                data.setKeyword1(new Content(paramIn.getString("title")));
                data.setKeyword2(new Content(paramIn.getString("startTime")));
                data.setKeyword3(new Content(StringUtil.delHtmlTag(paramIn.getString("context"))));
                data.setRemark(new Content("如有疑问请联系相关物业人员"));
                templateMessage.setData(BeanConvertUtil.beanCovertJson(data));
                //获取业主公众号地址
                String wechatUrl = UrlCache.getOwnerUrl();
                if (!StringUtil.isEmpty(wechatUrl) && wechatUrl.contains("?")) {
                    wechatUrl += ("&wAppId=" + weChatDto.getAppId());
                } else {
                    wechatUrl += ("?wAppId=" + weChatDto.getAppId());
                }
                templateMessage.setUrl(wechatUrl);
                logger.info("发送模板消息内容:{}", JSON.toJSONString(templateMessage));
                ResponseEntity<String> responseEntity = outRestTemplate.postForEntity(url, JSON.toJSONString(templateMessage), String.class);
                logger.info("微信模板返回内容:{}", responseEntity);
            }
        }
    }
}
