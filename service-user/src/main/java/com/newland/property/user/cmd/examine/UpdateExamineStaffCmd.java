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
package com.newland.property.user.cmd.examine;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.smo.IPhotoSMO;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.user.IExamineStaffIntroductionV1InnerServiceSMO;
import com.newland.property.intf.user.IExamineStaffProjectV1InnerServiceSMO;
import com.newland.property.intf.user.IExamineStaffV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.examine.ExamineStaffPo;
import com.newland.property.po.examine.ExamineStaffIntroductionPo;
import com.newland.property.po.examine.ExamineStaffProjectPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * 类表述：更新
 * 服务编码：examineStaff.updateExamineStaff
 * 请求路劲：/app/examineStaff.UpdateExamineStaff
 * add by 吴学文 at 2023-03-07 15:57:29 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "examine.updateExamineStaff")
public class UpdateExamineStaffCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdateExamineStaffCmd.class);


    @Autowired
    private IExamineStaffV1InnerServiceSMO examineStaffV1InnerServiceSMOImpl;

    @Autowired
    private IExamineStaffProjectV1InnerServiceSMO examineStaffProjectV1InnerServiceSMOImpl;


    @Autowired
    private IExamineStaffIntroductionV1InnerServiceSMO examineStaffIntroductionV1InnerServiceSMOImpl;

    @Autowired
    private IPhotoSMO photoSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "esId", "esId不能为空");
        Assert.hasKeyAndValue(reqJson, "staffId", "staffId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");

        if (!reqJson.containsKey("projectIds")) {
            throw new CmdException("没包含考核项目");
        }

        JSONArray projectIds = reqJson.getJSONArray("projectIds");

        if (projectIds == null || projectIds.size() < 1) {
            throw new CmdException("没包含考核项目");
        }
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        UserDto userDto = new UserDto();
        userDto.setUserId(reqJson.getString("staffId"));
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "员工不存在");

        ExamineStaffPo examineStaffPo = BeanConvertUtil.covertBean(reqJson, ExamineStaffPo.class);
        examineStaffPo.setStaffName(userDtos.get(0).getName());
        int flag = examineStaffV1InnerServiceSMOImpl.updateExamineStaff(examineStaffPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        ExamineStaffProjectPo tmpExamineStaffProjectPo = null;
        tmpExamineStaffProjectPo = new ExamineStaffProjectPo();
        tmpExamineStaffProjectPo.setEsId(examineStaffPo.getEsId());
        tmpExamineStaffProjectPo.setCommunityId(examineStaffPo.getCommunityId());
        examineStaffProjectV1InnerServiceSMOImpl.deleteExamineStaffProject(tmpExamineStaffProjectPo);


        JSONArray projectIds = reqJson.getJSONArray("projectIds");

        for (int projectIndex = 0; projectIndex < projectIds.size(); projectIndex++) {
            tmpExamineStaffProjectPo = new ExamineStaffProjectPo();
            tmpExamineStaffProjectPo.setEsId(examineStaffPo.getEsId());
            tmpExamineStaffProjectPo.setCommunityId(examineStaffPo.getCommunityId());
            tmpExamineStaffProjectPo.setProjectId(projectIds.getString(projectIndex));
            tmpExamineStaffProjectPo.setEspId(GenerateCodeFactory.getGeneratorId("11"));
            examineStaffProjectV1InnerServiceSMOImpl.saveExamineStaffProject(tmpExamineStaffProjectPo);
        }

        // todo save examine staff introduction
        if (reqJson.containsKey("introduction") && !StringUtil.isEmpty(reqJson.getString("introduction"))) {
            ExamineStaffIntroductionPo examineStaffIntroductionPo = new ExamineStaffIntroductionPo();
            examineStaffIntroductionPo.setStaffId(reqJson.getString("staffId"));
            examineStaffIntroductionPo.setIntroduction(reqJson.getString("introduction"));
            examineStaffIntroductionPo.setCommunityId(reqJson.getString("communityId"));
            examineStaffIntroductionV1InnerServiceSMOImpl.updateExamineStaffIntroduction(examineStaffIntroductionPo);
        }
        // todo save examine staff face image
        if(reqJson.containsKey("headerImg")) {

            photoSMOImpl.savePhoto(reqJson.getString("headerImg"),
                    examineStaffPo.getEsId(),
                    reqJson.getString("communityId"));
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());


    }
}
