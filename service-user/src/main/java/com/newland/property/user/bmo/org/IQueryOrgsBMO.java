package com.newland.property.user.bmo.org;

import com.newland.property.dto.org.OrgDto;

import java.util.List;

/**
 * @ClassName IQueryOrgsService
 * @Description TODO 查询组织信息
 * @Author wuxw
 * @Date 2020/7/12 20:04
 * @Version 1.0
 * add by wuxw 2020/7/12
 **/
public interface IQueryOrgsBMO {

    /**
     * 查询组织信息
     * @param orgDto
     * @return
     */
    List<OrgDto> queryOrgs(OrgDto orgDto);
}
