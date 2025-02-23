package com.newland.property.user.bmo.org.impl;

import com.newland.property.dto.org.OrgDto;
import com.newland.property.intf.user.IOrgInnerServiceSMO;
import com.newland.property.user.bmo.org.IQueryOrgsBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName QueryOrgsServiceImpl
 * @Description TODO
 * @Author wuxw
 * @Date 2020/7/12 20:06
 * @Version 1.0
 * add by wuxw 2020/7/12
 **/
@Service(value = "queryOrgsBMOImpl")
public class QueryOrgsBMOImpl implements IQueryOrgsBMO {

    @Autowired
    private IOrgInnerServiceSMO orgInnerServiceSMOImpl;

    @Override
    public List<OrgDto> queryOrgs(OrgDto orgDto) {
        List<OrgDto> orgs = orgInnerServiceSMOImpl.queryOrgs(orgDto);
        return orgs;
    }
}
