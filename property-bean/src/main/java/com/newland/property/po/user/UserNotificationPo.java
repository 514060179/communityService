package com.newland.property.po.user;

import java.io.Serializable;

public class UserNotificationPo implements Serializable {

    private String communityId;

    private String userId;

    private int notificationId;

    private String name;

    private String title;

    private String content;

    private int type;

    private int pageType;

    private String bgImage;

    private String appPageUrl;

    private String h5PageUrl;

    private String senderName = "係統通知";

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPageType() {
        return pageType;
    }

    public void setPageType(int pageType) {
        this.pageType = pageType;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public String getAppPageUrl() {
        return appPageUrl;
    }

    public void setAppPageUrl(String appPageUrl) {
        this.appPageUrl = appPageUrl;
    }

    public String getH5PageUrl() {
        return h5PageUrl;
    }

    public void setH5PageUrl(String h5PageUrl) {
        this.h5PageUrl = h5PageUrl;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
