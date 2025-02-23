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
package com.newland.property.community.cmd.maintainanceTask;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IMaintainanceTaskDetailV1InnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import com.newland.property.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import com.newland.property.dto.maintainance.MaintainanceTaskDetailDto;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类表述：查询
 * 服务编码：maintainanceTaskDetail.listMaintainanceTaskDetail
 * 请求路劲：/app/maintainanceTaskDetail.ListMaintainanceTaskDetail
 * add by 吴学文 at 2022-11-08 16:02:23 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "maintainanceTask.listMaintainanceTaskDetail")
public class ListMaintainanceTaskDetailCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(ListMaintainanceTaskDetailCmd.class);

    @Autowired
    private IMaintainanceTaskDetailV1InnerServiceSMO maintainanceTaskDetailV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "未包含小区");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        MaintainanceTaskDetailDto maintainanceTaskDetailDto = BeanConvertUtil.covertBean(reqJson, MaintainanceTaskDetailDto.class);
        int count = maintainanceTaskDetailV1InnerServiceSMOImpl.queryMaintainanceTaskDetailsCount(maintainanceTaskDetailDto);
        List<MaintainanceTaskDetailDto> maintainanceTaskDetailDtos = null;
        if (count > 0) {
            maintainanceTaskDetailDtos = maintainanceTaskDetailV1InnerServiceSMOImpl.queryMaintainanceTaskDetails(maintainanceTaskDetailDto);
            refreshPhotos(maintainanceTaskDetailDtos);
        } else {
            maintainanceTaskDetailDtos = new ArrayList<>();
        }
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, maintainanceTaskDetailDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        cmdDataFlowContext.setResponseEntity(responseEntity);
    }

    private void refreshPhotos(List<MaintainanceTaskDetailDto> maintainanceTaskDetailDtos) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");
        for (MaintainanceTaskDetailDto inspectionTaskDetail : maintainanceTaskDetailDtos) {
            if (!"20200407".equals(inspectionTaskDetail.getState())) {
                continue;
            }
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(inspectionTaskDetail.getTaskDetailId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName().startsWith("http") ? tmpFileRelDto.getFileRealName() : imgUrl + tmpFileRelDto.getFileRealName());
                photoVos.add(photoVo);
            }
            inspectionTaskDetail.setPhotos(photoVos);
        }
    }
}
