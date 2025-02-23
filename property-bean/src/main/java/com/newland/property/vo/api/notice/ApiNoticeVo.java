package com.newland.property.vo.api.notice;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiNoticeVo extends MorePageVo implements Serializable {
    List<ApiNoticeDataVo> notices;


    public List<ApiNoticeDataVo> getNotices() {
        return notices;
    }

    public void setNotices(List<ApiNoticeDataVo> notices) {
        this.notices = notices;
    }
}
