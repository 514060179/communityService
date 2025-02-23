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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.menu.MenuGroupDto;
import com.newland.property.dto.store.StoreAttrDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.dto.oaWorkflow.WorkflowDto;
import com.newland.property.intf.common.IWorkflowV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunityV1InnerServiceSMO;
import com.newland.property.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreAttrV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreUserV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreV1InnerServiceSMO;
import com.newland.property.intf.user.*;
import com.newland.property.po.community.CommunityMemberPo;
import com.newland.property.po.menu.MenuGroupCommunityPo;
import com.newland.property.po.org.OrgPo;
import com.newland.property.po.org.OrgStaffRelPo;
import com.newland.property.po.privilege.PrivilegeUserPo;
import com.newland.property.po.store.StoreAttrPo;
import com.newland.property.po.store.StorePo;
import com.newland.property.po.store.StoreUserPo;
import com.newland.property.po.user.UserPo;
import com.newland.property.po.oaWorkflow.WorkflowPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.constant.StateConstant;
import com.newland.property.utils.constant.StoreUserRelConstant;
import com.newland.property.utils.constant.UserLevelConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@NewlandPropertyCmdDoc(title = "添加物业公司",
        description = "主要提供给外系统添加物业公司",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/property.saveProperty",
        resource = "storeDoc",
        author = "吴学文",
        serviceCode = "property.saveProperty",
        seq = 1
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "name", length = 64, remark = "物业名称"),
        @NewlandPropertyParamDoc(name = "nearbyLandmarks", length = 64, remark = "地标，如xx 公园旁"),
        @NewlandPropertyParamDoc(name = "tel", length = 11, remark = "物业管理员电话 作为管理员账号密码 添加后请及时修改密码"),
        @NewlandPropertyParamDoc(name = "address", length = 11, remark = "公司地址"),
        @NewlandPropertyParamDoc(name = "corporation", length = 11, remark = "法人"),
        @NewlandPropertyParamDoc(name = "foundingTime", length = 11, remark = "成立日期"),
        @NewlandPropertyParamDoc(name = "communityIds", type = "Array" ,length = 0, remark = "分配小区"),
        @NewlandPropertyParamDoc(parentNodeName = "communityIds",name = "-", type = "String" ,length = 0, remark = "小区ID"),
        @NewlandPropertyParamDoc(name = "groupIds", type = "Array" ,length = 0, remark = "分配菜单组"),
        @NewlandPropertyParamDoc(parentNodeName = "groupIds",name = "-", type = "String" ,length = 0, remark = "菜单组ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"name\":\"api物业\",\"address\":\"api物业\",\"tel\":\"18909711449\",\"corporation\":\"无\",\"foundingTime\":\"2022-09-01\",\"nearbyLandmarks\":\"国投广场\",\"groupIds\":[\"802021080609660006\",\"802021012591650002\",\"802020101628950105\",\"802020092468300217\",\"802020091723050020\",\"802020020977260001\",\"802020012374230001\",\"802019110855900043\",\"802019103010680005\",\"802019102057730004\",\"802019091604450001\",\"800201907017\",\"800201906011\",\"800201904009\",\"800201904008\",\"800201904007\",\"800201904006\",\"800201904005\",\"800201904004\",\"800201906010\",\"800201904002\",\"800201904001\",\"802022052412780003\",\"802021080609660006\",\"802021012591650002\",\"802020101628950105\",\"802020092468300217\",\"802020091723050020\",\"802020020977260001\",\"802020012374230001\",\"802019110855900043\",\"802019103010680005\",\"802019102057730004\",\"802019091604450001\",\"800201907017\",\"800201906011\",\"800201904009\",\"800201904008\",\"800201904007\",\"800201904006\",\"800201904005\",\"800201904004\",\"800201906010\",\"800201904002\",\"800201904001\"],\"communityIds\":[\"2022092200930358\"]}",
        resBody="{'code':0,'msg':'成功'}"
)
/**
 * 类表述：保存
 * 服务编码：store.saveStore
 * 请求路劲：/app/store.SaveStore
 * add by 吴学文 at 2022-02-28 10:46:30 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "property.saveProperty")
public class SavePropertyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SavePropertyCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IStoreV1InnerServiceSMO storeV1InnerServiceSMOImpl;

    @Autowired
    private IStoreAttrV1InnerServiceSMO storeAttrV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IOrgV1InnerServiceSMO orgV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IWorkflowV1InnerServiceSMO workflowV1InnerServiceSMOImpl;

    @Autowired
    private IPrivilegeUserV1InnerServiceSMO privilegeUserV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityV1InnerServiceSMO communityV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupV1InnerServiceSMO menuGroupV1InnerServiceSMOImpl;

    @Autowired
    private IMenuGroupCommunityV1InnerServiceSMO menuGroupCommunityV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "name", "请求报文中未包含名称");
        Assert.hasKeyAndValue(reqJson, "address", "请求报文中未包含地址");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "corporation", "请求报文中未包含法人");
        Assert.hasKeyAndValue(reqJson, "foundingTime", "请求报文中未包含成立日期");

        // 判断是否包含了小区信息
        if (!reqJson.containsKey("communityIds") || reqJson.getJSONArray("communityIds").size() < 1) {
            return;
        }

        // 判断是否包含了小区信息
        if (!reqJson.containsKey("groupIds") || reqJson.getJSONArray("groupIds").size() < 1) {
            throw new CmdException("未包含小区开通功能");
        }

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        StorePo storePo = BeanConvertUtil.covertBean(reqJson, StorePo.class);
        storePo.setStoreId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        storePo.setStoreTypeCd(StoreDto.STORE_TYPE_PROPERTY);
        storePo.setState(StoreDto.STATE_NORMAL);
        if(!reqJson.containsKey("mapY")){
            storePo.setMapY("1");
        }
        if(!reqJson.containsKey("mapX")){
            storePo.setMapX("1");
        }
        int flag = storeV1InnerServiceSMOImpl.saveStore(storePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //保存属性
        StoreAttrPo storeAttrPo = new StoreAttrPo();
        storeAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
        storeAttrPo.setSpecCd(StoreAttrDto.SPEC_CD_CORPORATION);
        storeAttrPo.setValue(reqJson.getString("corporation"));
        storeAttrPo.setStoreId(storePo.getStoreId());
        flag = storeAttrV1InnerServiceSMOImpl.saveStoreAttr(storeAttrPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        storeAttrPo = new StoreAttrPo();
        storeAttrPo.setAttrId(GenerateCodeFactory.getAttrId());
        storeAttrPo.setSpecCd(StoreAttrDto.SPEC_CD_FOUNDINGTIME);
        storeAttrPo.setValue(reqJson.getString("foundingTime"));
        storeAttrPo.setStoreId(storePo.getStoreId());
        flag = storeAttrV1InnerServiceSMOImpl.saveStoreAttr(storeAttrPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //添加用户
        UserPo userPo = new UserPo();
        userPo.setTel(reqJson.getString("tel"));
        userPo.setAreaCode(reqJson.getString("areaCode"));
        userPo.setName(reqJson.getString("name"));
        userPo.setAddress(reqJson.getString("address"));
        userPo.setUserId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userId));
        userPo.setScore("0");
        userPo.setAge("1");
        userPo.setEmail("无");
        userPo.setLevelCd(UserLevelConstant.USER_LEVEL_ADMIN);
        userPo.setSex("1");
        userPo.setPassword(AuthenticationFactory.passwdMd5(reqJson.getString("tel")));
        userPo.setbId("-1");
        flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存 商户和用户的关系
        StoreUserPo storeUserPo = new StoreUserPo();
        storeUserPo.setStoreUserId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        storeUserPo.setRelCd(StoreUserRelConstant.REL_ADMIN);
        storeUserPo.setStoreId(storePo.getStoreId());
        storeUserPo.setUserId(userPo.getUserId());
        flag = storeUserV1InnerServiceSMOImpl.saveStoreUser(storeUserPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //保存公司级组织
        OrgPo orgPo = new OrgPo();
        orgPo.setOrgName(storePo.getName());
        orgPo.setOrgLevel("1");
        orgPo.setOrgId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_orgId));
        orgPo.setAllowOperation("F");
        orgPo.setBelongCommunityId("9999");
        orgPo.setParentOrgId("-1");
        orgPo.setStoreId(storePo.getStoreId());

        flag = orgV1InnerServiceSMOImpl.saveOrg(orgPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }


        //添加组织 员工关系
        OrgStaffRelPo orgStaffRelPo = new OrgStaffRelPo();
        orgStaffRelPo.setOrgId(orgPo.getOrgId());
        orgStaffRelPo.setStaffId(userPo.getUserId());
        orgStaffRelPo.setRelId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        orgStaffRelPo.setRelCd(StoreUserRelConstant.REL_ADMIN);
        orgStaffRelPo.setStoreId(storePo.getStoreId());
        flag = orgStaffRelV1InnerServiceSMOImpl.saveOrgStaffRel(orgStaffRelPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //合同申请续签
        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setCommunityId("9999"); //所有小区
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workflowPo.setFlowName("合同申请续签");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_CONTRACT_APPLY);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(storePo.getStoreId());
        flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        //合同变更
        workflowPo = new WorkflowPo();
        workflowPo.setCommunityId("9999"); //所有小区
        workflowPo.setFlowId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        workflowPo.setFlowName("合同变更");
        workflowPo.setFlowType(WorkflowDto.FLOW_TYPE_CONTRACT_CHANGE);
        workflowPo.setSkipLevel(WorkflowDto.DEFAULT_SKIP_LEVEL);
        workflowPo.setStoreId(storePo.getStoreId());
        flag = workflowV1InnerServiceSMOImpl.saveWorkflow(workflowPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        String defaultPrivilege = MappingCache.getValue(MappingConstant.DOMAIN_DEFAULT_PRIVILEGE_ADMIN, StoreDto.STORE_TYPE_PROPERTY);

        Assert.hasLength(defaultPrivilege, "未配置物业默认权限");
        PrivilegeUserPo privilegeUserPo = new PrivilegeUserPo();
        privilegeUserPo.setPrivilegeFlag("1");
        privilegeUserPo.setStoreId(storePo.getStoreId());
        privilegeUserPo.setUserId(userPo.getUserId());
        privilegeUserPo.setpId(defaultPrivilege);
        privilegeUserPo.setPuId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));

        flag = privilegeUserV1InnerServiceSMOImpl.savePrivilegeUser(privilegeUserPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        // 判断是否包含了小区信息
        if (!reqJson.containsKey("communityIds") || reqJson.getJSONArray("communityIds").size() < 1) {
            cmdDataFlowContext.setResponseEntity(ResultVo.success());
            return;
        }


        JSONArray communityIds = reqJson.getJSONArray("communityIds");

        String communityId;
        for (int communityIndex = 0; communityIndex < communityIds.size(); communityIndex++) {
            communityId = communityIds.getString(communityIndex);
            CommunityMemberDto communityMemberDto = new CommunityMemberDto();
            communityMemberDto.setCommunityId(communityId);
            communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
            List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

            if (communityMemberDtos != null && communityMemberDtos.size() > 0) {
                throw new CmdException("小区已经入驻" + communityId);
            }

            //查询小区是否存在
            CommunityDto communityDto = new CommunityDto();
            communityDto.setCommunityId(communityId);
            List<CommunityDto> communityDtos = communityV1InnerServiceSMOImpl.queryCommunitys(communityDto);

            Assert.listOnlyOne(communityDtos, "小区不存在");

            Calendar endTime = Calendar.getInstance();
            endTime.add(Calendar.MONTH, Integer.parseInt(communityDtos.get(0).getPayFeeMonth()));
            CommunityMemberPo communityMemberPo = new CommunityMemberPo();
            communityMemberPo.setCommunityMemberId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            communityMemberPo.setEndTime(DateUtil.getFormatTimeString(endTime.getTime(), DateUtil.DATE_FORMATE_STRING_A));
            communityMemberPo.setStartTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
            communityMemberPo.setAuditStatusCd(StateConstant.AGREE_AUDIT);
            communityMemberPo.setCommunityId(communityId);
            communityMemberPo.setMemberId(storePo.getStoreId());
            communityMemberPo.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
            flag = communityMemberV1InnerServiceSMOImpl.saveCommunityMember(communityMemberPo);
            if (flag < 1) {
                throw new CmdException("注册失败");
            }

            //保存小区开放功能

            saveMenuGroupCommunity(reqJson, communityId, communityDtos.get(0).getName());

            // 修改投诉建议
            flag = updateWorkflow(WorkflowDto.FLOW_TYPE_COMPLAINT, communityId, storePo.getStoreId());
            if (flag < 1) {
                continue;
            }

            // 修改物品领用
            flag = updateWorkflow(WorkflowDto.FLOW_TYPE_COLLECTION, communityId, storePo.getStoreId());
            if (flag < 1) {
                continue;
            }

            // 修改
            flag = updateWorkflow(WorkflowDto.FLOW_TYPE_ALLOCATION_STOREHOUSE_GO, communityId, storePo.getStoreId());
            if (flag < 1) {
                continue;
            }
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());



    }

    private void saveMenuGroupCommunity(JSONObject reqJson, String communityId, String communityName) {
        List<MenuGroupDto> menuGroupDtos = null;
        MenuGroupDto menuGroupDto = null;
        if (!reqJson.containsKey("groupIds") || reqJson.getJSONArray("groupIds").size() < 1) {
            menuGroupDto = new MenuGroupDto();
            menuGroupDto.setStoreType(StoreDto.STORE_TYPE_PROPERTY);
            menuGroupDtos = menuGroupV1InnerServiceSMOImpl.queryMenuGroups(menuGroupDto);
        } else {
            menuGroupDto = new MenuGroupDto();
            JSONArray groupIds = reqJson.getJSONArray("groupIds");
            String groupId;
            List<String> gIds = new ArrayList<>();
            for (int groupIndex = 0; groupIndex < groupIds.size(); groupIndex++) {
                groupId = groupIds.getString(groupIndex);
                gIds.add(groupId);
            }
            menuGroupDto.setgIds(gIds.toArray(new String[gIds.size()]));
            menuGroupDtos = menuGroupV1InnerServiceSMOImpl.queryMenuGroups(menuGroupDto);
        }

        if (menuGroupDtos == null || menuGroupDtos.size() < 1) {
            throw new IllegalArgumentException("没有分配任何功能");
        }

        List<MenuGroupCommunityPo> menuGroupCommunityPos = new ArrayList<>();
        MenuGroupCommunityPo tmpMenuGroupCommunityPo = null;
        for (MenuGroupDto menuGroupDto1 : menuGroupDtos) {
            tmpMenuGroupCommunityPo = new MenuGroupCommunityPo();
            tmpMenuGroupCommunityPo.setCommunityId(communityId);
            tmpMenuGroupCommunityPo.setCommunityName(communityName);
            tmpMenuGroupCommunityPo.setGcId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            tmpMenuGroupCommunityPo.setgId(menuGroupDto1.getgId());
            tmpMenuGroupCommunityPo.setName(menuGroupDto1.getName());
            menuGroupCommunityPos.add(tmpMenuGroupCommunityPo);
        }
        int flag = menuGroupCommunityV1InnerServiceSMOImpl.saveMenuGroupCommunitys(menuGroupCommunityPos);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }
    }

    /**
     * 刷入小区ID
     *
     * @param flowType 接口请求数据封装
     * @return 封装好的 data数据
     */
    public int updateWorkflow(String flowType, String communityId, String storeId) {
        WorkflowDto workflowDto = new WorkflowDto();
        workflowDto.setCommunityId(communityId);
        workflowDto.setFlowType(flowType);
        List<WorkflowDto> workflowDtos = workflowV1InnerServiceSMOImpl.queryWorkflows(workflowDto);

        if (workflowDtos == null || workflowDtos.size() < 1) {
            return 0;
        }
        WorkflowPo workflowPo = new WorkflowPo();
        workflowPo.setFlowId(workflowDtos.get(0).getFlowId());
        workflowPo.setCommunityId(communityId);
        workflowPo.setStoreId(storeId);
        return workflowV1InnerServiceSMOImpl.updateWorkflow(workflowPo);
    }
}
