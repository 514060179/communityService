/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.order.smo.impl;

import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.privilege.PrivilegeUserDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.order.IPrivilegeInnerServiceSMO;
import com.newland.property.order.dao.IPrivilegeDAO;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * privilege inner transaction class
 * <p>
 * Created by wuxw on 2019/4/1.
 */

@RestController
public class PrivilegeInnerServiceSMOImpl extends BaseServiceSMO implements IPrivilegeInnerServiceSMO {

    @Autowired
    private IPrivilegeDAO privilegeDAOImpl;

    @Override
    public List<UserDto> queryPrivilegeUsers(@RequestBody BasePrivilegeDto privilegeDto) {
        List<Map> userIds = privilegeDAOImpl.queryPrivilegeUsers(BeanConvertUtil.beanCovertMap(privilegeDto));
        return BeanConvertUtil.covertBeanList(userIds, UserDto.class);
    }

    @Override
    public List<PrivilegeUserDto> queryPrivilegeUser(@RequestBody PrivilegeUserDto privilegeUserDto) {
        List<Map> privilegeUsers = privilegeDAOImpl.queryUserPrivilege(BeanConvertUtil.beanCovertMap(privilegeUserDto));
        return BeanConvertUtil.covertBeanList(privilegeUsers, PrivilegeUserDto.class);
    }

}
