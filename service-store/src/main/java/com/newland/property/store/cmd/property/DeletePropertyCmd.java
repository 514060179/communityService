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
package com.newland.property.store.cmd.property;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.Environment;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.store.StoreUserDto;
import com.newland.property.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreUserV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.intf.user.IMenuGroupCommunityV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.community.CommunityMemberPo;
import com.newland.property.po.menu.MenuGroupCommunityPo;
import com.newland.property.po.store.StorePo;
import com.newland.property.po.store.StoreUserPo;
import com.newland.property.po.user.UserPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;



@NewlandPropertyCmdDoc(title = "删除物业公司",
        description = "主要提供给外系统删除物业公司",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/property.deleteProperty",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "property.deleteProperty",
        seq = 3
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "storeId", length = 30, remark = "物业编号"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"storeId\":\"102022083062960025\"}",
        resBody="{'code':0,'msg':'成功'}"
)

/**
 * 类表述：删除
 * 服务编码：store.deleteStore
 * 请求路劲：/app/store.DeleteStore
 * add by 吴学文 at 2022-02-28 10:46:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "property.deleteProperty")
public class DeletePropertyCmd extends Cmd {
    private static Logger logger = LoggerFactory.getLogger(DeletePropertyCmd.class);

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupCommunityV1InnerServiceSMO menuGroupCommunityV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Environment.isDevEnv();
        Assert.hasKeyAndValue(reqJson, "storeId", "storeId不能为空");
        Assert.hasKeyAndValue(reqJson, "storeTypeCd", "storeTypeCd不能为空");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        StorePo storePo = BeanConvertUtil.covertBean(reqJson, StorePo.class);
        int flag = storeV1InnerServiceSMOImpl.deleteStore(storePo);

        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }

        // todo 删除 物业下的所有员工数据
        deleteStaff(storePo);

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(storePo.getStoreId());
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        if (communityMemberDtos == null || communityMemberDtos.size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }

        //释放小区
        CommunityMemberPo communityMemberPo = new CommunityMemberPo();
        communityMemberPo.setMemberId(storePo.getStoreId());
        flag = communityMemberV1InnerServiceSMOImpl.deleteCommunityMember(communityMemberPo);
        if (flag < 1) {
            throw new CmdException("释放小区失败");
        }

        //小区权限也踢掉
        for (CommunityMemberDto tmpCommunityMemberDto : communityMemberDtos) {
            MenuGroupCommunityPo menuGroupCommunityPo = new MenuGroupCommunityPo();
            menuGroupCommunityPo.setCommunityId(tmpCommunityMemberDto.getCommunityId());
            flag = menuGroupCommunityV1InnerServiceSMOImpl.deleteMenuGroupCommunity(menuGroupCommunityPo);
            if (flag < 1) {
                throw new CmdException("小区权限踢掉失败");
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 删除 员工信息
     * @param storePo
     */
    private void deleteStaff(StorePo storePo) {
        StoreUserDto storeUserDto = new StoreUserDto();
        storeUserDto.setStoreId(storePo.getStoreId());
        List<StoreUserDto> storeUserDtos = storeUserV1InnerServiceSMOImpl.queryStoreUsers(storeUserDto);
        if(storeUserDtos == null || storeUserDtos.size() < 1){
            return;
        }
        StoreUserPo storeUserPo = new StoreUserPo();
        storeUserPo.setStoreId(storePo.getStoreId());
        storeUserV1InnerServiceSMOImpl.deleteStoreUser(storeUserPo);
        UserPo userPo = null;
        for(StoreUserDto staff : storeUserDtos){
            userPo = new UserPo();
            userPo.setUserId(staff.getUserId());
            userV1InnerServiceSMOImpl.deleteUser(userPo);
        }
    }
}
