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
package com.newland.property.user.cmd.junkRequirement;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.user.IJunkRequirementV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.junkRequirement.JunkRequirementPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：保存
 * 服务编码：junkRequirement.saveJunkRequirement
 * 请求路劲：/app/junkRequirement.SaveJunkRequirement
 * add by 吴学文 at 2022-08-08 08:53:50 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "junkRequirement.saveJunkRequirement")
public class SaveJunkRequirementCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveJunkRequirementCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IJunkRequirementV1InnerServiceSMO junkRequirementV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "typeCd", "请求报文中未包含typeCd");
        Assert.hasKeyAndValue(reqJson, "classification", "请求报文中未包含classification");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");
        Assert.hasKeyAndValue(reqJson, "referencePrice", "请求报文中未包含referencePrice");
        Assert.hasKeyAndValue(reqJson, "publishUserId", "请求报文中未包含publishUserId");
        Assert.hasKeyAndValue(reqJson, "publishUserName", "请求报文中未包含publishUserName");
        Assert.hasKeyAndValue(reqJson, "publishUserLink", "请求报文中未包含publishUserLink");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //reqJson.put("state", "12001");
        reqJson.put("state", "13001");
        String junkRequirementId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_junkRequirementId);
        reqJson.put("junkRequirementId", junkRequirementId);

        if (reqJson.containsKey("photos")) {
            dealPhotos(reqJson);
        }

       JunkRequirementPo junkRequirementPo = BeanConvertUtil.covertBean(reqJson, JunkRequirementPo.class);
        junkRequirementPo.setJunkRequirementId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = junkRequirementV1InnerServiceSMOImpl.saveJunkRequirement(junkRequirementPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    private void dealPhotos(JSONObject reqJson) {
        JSONArray photos = reqJson.getJSONArray("photos");
        JSONObject photo = null;
        int flag = 0;
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            photo = photos.getJSONObject(photoIndex);
            FileDto fileDto = new FileDto();
            fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            fileDto.setFileName(fileDto.getFileId());
            fileDto.setContext(photo.getString("photo"));
            fileDto.setSuffix("jpeg");
            fileDto.setCommunityId(reqJson.getString("communityId"));
            String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
            reqJson.put("photoId", fileDto.getFileId());
            reqJson.put("fileSaveName", fileName);

            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            businessUnit.put("relTypeCd", "80000");
            businessUnit.put("saveWay", "ftp");
            businessUnit.put("objId", reqJson.getString("junkRequirementId"));
            businessUnit.put("fileRealName", reqJson.getString("photoId"));
            businessUnit.put("fileSaveName", reqJson.getString("fileSaveName"));
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);

            flag = fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            if(flag <1){
                throw new CmdException("报错图片异常");
            }
        }
    }
}
