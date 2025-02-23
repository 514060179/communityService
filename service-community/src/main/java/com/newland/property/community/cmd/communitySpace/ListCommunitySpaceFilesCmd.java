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
package com.newland.property.community.cmd.communitySpace;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.doc.annotation.*;
import com.newland.property.dto.community.CommunitySpaceDto;
import com.newland.property.dto.community.CommunitySpaceFileDto;
import com.newland.property.dto.community.CommunitySpaceOpenTimeDto;
import com.newland.property.intf.community.ICommunitySpaceOpenTimeV1InnerServiceSMO;
import com.newland.property.intf.community.ICommunitySpaceV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmdDoc(title = "查询场地文件",
        description = "查询系统中的查询场地文件",
        httpMethod = "get",
        url = "http://{ip}:{port}/app/communitySpace.listCommunitySpaceFiles",
        resource = "communityDoc",
        author = "Moonny",
        serviceCode = "communitySpace.listCommunitySpaceFiles"
)

@NewlandPropertyParamsDoc(params = {
        @NewlandPropertyParamDoc(name = "page", type = "int", length = 11, remark = "分页页数"),
        @NewlandPropertyParamDoc(name = "row", type = "int", length = 11, remark = "分页行数"),
        @NewlandPropertyParamDoc(name = "type", type = "int", length = 11, remark = "類型 1 守則 2 價目表"),
        @NewlandPropertyParamDoc(name = "communityId", length = 30, remark = "小区ID"),
})

@NewlandPropertyResponseDoc(
        params = {
                @NewlandPropertyParamDoc(name = "code", type = "int", length = 11, defaultValue = "0", remark = "返回编号，0 成功 其他失败"),
                @NewlandPropertyParamDoc(name = "msg", type = "String", length = 250, defaultValue = "成功", remark = "描述"),
                @NewlandPropertyParamDoc(name = "data", type = "Array", remark = "有效数据"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "name", type = "String", remark = "场地名称"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "img", type = "String", remark = "場地圖片"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "fileName", type = "String", remark = "文件名稱"),
                @NewlandPropertyParamDoc(parentNodeName = "data", name = "filePath", type = "String", remark = "文件路徑"),
        }
)

@NewlandPropertyExampleDoc(
        reqBody = "http://{ip}:{port}/app/communitySpace" +
                ".listCommunitySpaceFiles?&communityId=2022081539020475&page=1&row=10&type=1",
        resBody = ""
)

@NewlandPropertyCmd(serviceCode = "communitySpace.listCommunitySpaceFiles")
public class ListCommunitySpaceFilesCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListCommunitySpaceFilesCmd.class);
    @Autowired
    private ICommunitySpaceV1InnerServiceSMO communitySpaceV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        CommunitySpaceFileDto communitySpaceFileDto = BeanConvertUtil.covertBean(reqJson, CommunitySpaceFileDto.class);

        int count = communitySpaceV1InnerServiceSMOImpl.queryCommunitySpaceFilesCount(communitySpaceFileDto);

        List<CommunitySpaceFileDto> communitySpaceFileDtos = null;

        if (count > 0) {
            communitySpaceFileDtos = communitySpaceV1InnerServiceSMOImpl.queryCommunitySpaceFiles(communitySpaceFileDto);
        } else {
            communitySpaceFileDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, communitySpaceFileDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        cmdDataFlowContext.setResponseEntity(responseEntity);
    }
}
