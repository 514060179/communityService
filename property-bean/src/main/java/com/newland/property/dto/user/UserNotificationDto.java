package com.newland.property.dto.user;

import com.newland.property.dto.PageDto;
import java.io.Serializable;
import java.util.Date;

public class UserNotificationDto extends PageDto implements Serializable {

    private int id;

    private String communityId;

    private String userId;

    private int notificationId;

    private String name;

    private String title;

    private String content;

    private int type = -1;

    private int pageType;

    private String bgImage;

    private String appPageUrl;

    private String h5PageUrl;

    private Date sentTime;

    private Date createTime;

    private Date updateTime;

    private int isDel = 0;

    private int isRead = -1;

    private Date readTime;

    private String senderName;

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

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
