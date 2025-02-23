package com.newland.property.user.cmd.user;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.AuthenticationFactory;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.store.IOrgStaffRelV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreUserV1InnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.org.OrgStaffRelPo;
import com.newland.property.po.store.StoreUserPo;
import com.newland.property.po.user.UserPo;
import com.newland.property.doc.annotation.*;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.constant.StoreUserRelConstant;
import com.newland.property.utils.constant.UserLevelConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@NewlandPropertyCmdDoc(title = "添加员工",
        description = "外部系统通过添加员工接口 添加员工，注意需要物业管理员账号登录，因为不需要传storeId 是根据管理员登录信息获取的",
        httpMethod = "post",
        url = "http://{ip}:{port}/app/user.staff.add",
        resource = "userDoc",
        author = "吴学文",
        serviceCode = "user.staff.add",
        seq = 3
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "orgId", length = 30, remark = "组织ID"),
        @NewlandPropertyParamDoc(name = "orgName", length = 64, remark = "组织名称"),
        @NewlandPropertyParamDoc(name = "name", length = 64, remark = "名称"),
        @NewlandPropertyParamDoc(name = "sex", length = 64, remark = "性别 0女 1男"),
        @NewlandPropertyParamDoc(name = "email", length = 64, remark = "邮箱"),
        @NewlandPropertyParamDoc(name = "tel", length = 11, remark = "手机号"),
        @NewlandPropertyParamDoc(name = "address", length = 64, remark = "地址"),
        @NewlandPropertyParamDoc(name = "relCd", length = 64, remark = "岗位,普通员工 1000 部门经理 2000 部门副经理 3000 部门组长 4000 分公司总经理 5000 分公司副总经理 6000 总经理助理 7000 总公司总经理 8000 总公司副总经理 9000"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody="{\"orgId\":\"102022091988250052\",\"orgName\":\"演示物业 / 件部\",\"username\":\"张三\",\"sex\":\"0\",\"email\":\"231@qq.com\",\"tel\":\"123\",\"address\":\"123\",\"relCd\":\"1000\",\"photo\":\"\",\"name\":\"张三\"}",
        resBody="{'code':0,'msg':'成功'}"
)

@NewlandPropertyCmd(serviceCode = "user.staff.add")
public class UserStaffAddCmd extends Cmd {

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IOrgStaffRelV1InnerServiceSMO orgStaffRelV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        if (!reqJson.containsKey("storeId")) {
            String storeId = context.getReqHeaders().get("store-id");
            reqJson.put("storeId", storeId);
        }
        //获取数据上下文对象
        Assert.jsonObjectHaveKey(reqJson, "storeId", "请求参数中未包含storeId 节点，请确认");
        //判断员工手机号是否重复(员工可根据手机号登录平台)
        UserDto userDto = new UserDto();
        userDto.setTel(reqJson.getString("tel"));
        userDto.setUserFlag("1");
        userDto.setLevelCd("01"); //员工
        List<UserDto> users = userInnerServiceSMOImpl.getUsers(userDto);
        Assert.listIsNull(users, "员工手机号不能重复，请重新输入");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String userId = "";

        String relCd = reqJson.getString("relCd");//员工 组织 岗位

        userId = GenerateCodeFactory.getUserId();
        reqJson.put("userId", userId);
        //添加用户
        addUser(reqJson);
        reqJson.put("userId", userId);
        if (!reqJson.containsKey("relCd") || StringUtil.isEmpty(reqJson.getString("relCd"))) {
            reqJson.put("relCd", StoreUserRelConstant.REL_COMMON);
        }
        addStaff(reqJson);
        //重写 员工岗位
        reqJson.put("relCd", relCd);
        addStaffOrg(reqJson);
        int flag = 0;
        if (reqJson.containsKey("photo") && !StringUtils.isEmpty(reqJson.getString("photo"))) {
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "12000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", userId);
            businessUnit.put("fileRealName", reqJson.getString("photo"));
            businessUnit.put("fileSaveName", reqJson.getString("photo"));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if (flag < 1) {
                throw new CmdException("保存图片异常");
            }
        }
    }

    /**
     * 添加用户
     *
     * @param paramObj
     */
    public void addUser(JSONObject paramObj) {

        //校验json 格式中是否包含 name,email,levelCd,tel
        Assert.jsonObjectHaveKey(paramObj, "name", "请求参数中未包含name 节点，请确认");
        //Assert.jsonObjectHaveKey(paramObj,"email","请求参数中未包含email 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj, "tel", "请求参数中未包含tel 节点，请确认");
        Assert.jsonObjectHaveKey(paramObj, "address", "请求报文格式错误或未包含地址信息");
        Assert.jsonObjectHaveKey(paramObj, "sex", "请求报文格式错误或未包含性别信息");


        if (paramObj.containsKey("email") && !StringUtil.isEmpty(paramObj.getString("email"))) {
            Assert.isEmail(paramObj, "email", "不是有效的邮箱格式");
        }


        UserPo userPo = BeanConvertUtil.covertBean(refreshParamIn(paramObj), UserPo.class);
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);

        if (flag < 1) {
            throw new CmdException("保存用户异常");
        }
    }

    /**
     * 对请求报文处理
     *
     * @param paramObj
     * @return
     */
    private JSONObject refreshParamIn(JSONObject paramObj) {
        //paramObj.put("userId","-1");
        paramObj.put("levelCd", UserLevelConstant.USER_LEVEL_STAFF);
        //设置默认密码
        String staffDefaultPassword = MappingCache.getValue(MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        Assert.hasLength(staffDefaultPassword, "映射表中未设置员工默认密码，请检查" + MappingConstant.KEY_STAFF_DEFAULT_PASSWORD);
        staffDefaultPassword = AuthenticationFactory.passwdMd5(staffDefaultPassword);
        paramObj.put("password", staffDefaultPassword);
        return paramObj;
    }

    /**
     * 添加员工
     *
     * @param paramInJson
     * @return
     */
    public void addStaff(JSONObject paramInJson) {

        JSONObject businessStoreUser = new JSONObject();
        businessStoreUser.put("storeId", paramInJson.getString("storeId"));
        businessStoreUser.put("storeUserId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_storeUserId));
        businessStoreUser.put("userId", paramInJson.getString("userId"));
        businessStoreUser.put("relCd", paramInJson.getString("relCd"));

        StoreUserPo storeUserPo = BeanConvertUtil.covertBean(businessStoreUser, StoreUserPo.class);
        int flag = storeUserV1InnerServiceSMOImpl.saveStoreUser(storeUserPo);

        if (flag < 1) {
            throw new CmdException("保存员工 失败");
        }
    }

    public void addStaffOrg(JSONObject paramInJson) {

        if (!paramInJson.containsKey("orgId") || StringUtil.isEmpty(paramInJson.getString("orgId"))) {
            return;
        }

        JSONObject businessOrgStaffRel = new JSONObject();
        businessOrgStaffRel.put("relId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
        businessOrgStaffRel.put("storeId", paramInJson.getString("storeId"));
        businessOrgStaffRel.put("staffId", paramInJson.getString("userId"));
        businessOrgStaffRel.put("orgId", paramInJson.getString("orgId"));
        businessOrgStaffRel.put("relCd", paramInJson.getString("relCd"));
        OrgStaffRelPo orgStaffRelPo = BeanConvertUtil.covertBean(businessOrgStaffRel, OrgStaffRelPo.class);
        int flag = orgStaffRelV1InnerServiceSMOImpl.saveOrgStaffRel(orgStaffRelPo);
        if (flag < 1) {
            throw new CmdException("保存员工 失败");
        }
    }
}
