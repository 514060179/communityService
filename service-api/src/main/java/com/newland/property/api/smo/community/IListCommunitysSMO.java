package com.newland.property.api.smo.community;

import com.newland.property.core.context.IPageData;
import com.newland.property.utils.exception.SMOException;
import org.springframework.http.ResponseEntity;

/**
 * 小区管理服务接口类
 *
 * add by wuxw 2019-06-29
 */
public interface IListCommunitysSMO {

    /**
     * 查询小区信息
     * @param pd 页面数据封装
     * @return ResponseEntity 对象数据
     * @throws SMOException 业务代码层
     */
    ResponseEntity<String> listCommunitys(IPageData pd) throws SMOException;
}
