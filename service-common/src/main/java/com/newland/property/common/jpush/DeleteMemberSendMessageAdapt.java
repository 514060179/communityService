package com.newland.property.common.jpush;

import com.newland.property.core.jpush.JPushMessageQueue;
import com.newland.property.dto.msg.JPushMessageDto;
import com.newland.property.intf.jpush.ISendJPushMessageAdapt;
import com.newland.property.intf.user.IUserNotificationInnerServiceSMO;
import com.newland.property.po.user.UserNotificationPo;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.JPushConstant;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Moonny
 */
@Service("deleteMemberSendMessageAdapt")
public class DeleteMemberSendMessageAdapt implements ISendJPushMessageAdapt {

    private static final String DEFAULT_TITLE = "家庭成员";

    private static final String DEFAULT_CONTENT = "删除家庭成员成功";

    private static final String DEFAULT_APP_URL = "/pages/family/familyList";

    @Autowired
    private IUserNotificationInnerServiceSMO userNotificationInnerServiceSMOImpl;

    @Override
    public void sendMessage(String communityId, String userId) {
        String title = MappingCache.getValue(JPushConstant.JPUSH_DOMAIN, JPushConstant.DELETE_MEMBER_MESSAGE_TITLE);
        String content = MappingCache.getValue(JPushConstant.JPUSH_DOMAIN, JPushConstant.DELETE_MEMBER_MESSAGE_CONTENT);
        String appUrl = MappingCache.getValue(JPushConstant.JPUSH_DOMAIN, JPushConstant.DELETE_MEMBER_MESSAGE_APP_URL);

        if (StringUtil.isNullOrNone(title)) {
            title = DEFAULT_TITLE;
        }

        if (StringUtil.isNullOrNone(content)) {
            content = DEFAULT_CONTENT;
        }

        if (StringUtil.isNullOrNone(appUrl)) {
            appUrl = DEFAULT_APP_URL;
        }

        JPushMessageDto messageDto = new JPushMessageDto();
        messageDto.setCommunityId(communityId);
        messageDto.setUserId(userId);
        messageDto.setPageUrl(appUrl);
        messageDto.setTitle(title);
        messageDto.setContent(content);
        messageDto.setPageType("appPage");
        JPushMessageQueue.addMsg(messageDto);

        UserNotificationPo userNotificationPo = new UserNotificationPo();
        userNotificationPo.setCommunityId(communityId);
        userNotificationPo.setUserId(userId);
        userNotificationPo.setNotificationId(0);
        userNotificationPo.setName(title);
        userNotificationPo.setTitle(title);
        userNotificationPo.setContent(content);
        userNotificationPo.setPageType(0);
        userNotificationPo.setAppPageUrl(appUrl);
        userNotificationPo.setH5PageUrl("");
        userNotificationPo.setBgImage("");
        userNotificationPo.setType(1);
        userNotificationInnerServiceSMOImpl.saveUserNotification(userNotificationPo);
    }
}
