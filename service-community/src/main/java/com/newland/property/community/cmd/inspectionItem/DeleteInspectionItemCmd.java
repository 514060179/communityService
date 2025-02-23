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
package com.newland.property.community.cmd.inspectionItem;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.inspection.InspectionItemTitleDto;
import com.newland.property.dto.inspection.InspectionItemTitleValueDto;
import com.newland.property.dto.inspection.InspectionDto;
import com.newland.property.intf.community.IInspectionItemTitleV1InnerServiceSMO;
import com.newland.property.intf.community.IInspectionItemTitleValueV1InnerServiceSMO;
import com.newland.property.intf.community.IInspectionItemV1InnerServiceSMO;
import com.newland.property.intf.community.IInspectionPointV1InnerServiceSMO;
import com.newland.property.po.inspection.InspectionItemPo;
import com.newland.property.po.inspection.InspectionItemTitlePo;
import com.newland.property.po.inspection.InspectionItemTitleValuePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 类表述：删除
 * 服务编码：inspectionItem.deleteInspectionItem
 * 请求路劲：/app/inspectionItem.DeleteInspectionItem
 * add by 吴学文 at 2022-03-15 10:55:20 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "inspectionItem.deleteInspectionItem")
public class DeleteInspectionItemCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(DeleteInspectionItemCmd.class);

    @Autowired
    private IInspectionItemV1InnerServiceSMO inspectionItemV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionPointV1InnerServiceSMO iInspectionPointV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionItemTitleV1InnerServiceSMO iInspectionItemTitleV1InnerServiceSMOImpl;

    @Autowired
    private IInspectionItemTitleValueV1InnerServiceSMO iInspectionItemTitleValueV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "itemId", "itemId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        InspectionItemPo inspectionItemPo = BeanConvertUtil.covertBean(reqJson, InspectionItemPo.class);
        InspectionDto inspectionDto = new InspectionDto();
        inspectionDto.setItemId(inspectionItemPo.getItemId());
        List<InspectionDto> inspectionDtos = iInspectionPointV1InnerServiceSMOImpl.queryInspectionPoints(inspectionDto);
        if (inspectionDtos != null && inspectionDtos.size() > 0) {
            throw new IllegalArgumentException("该巡检项目下有巡检点，请先删除巡检点！");
        }
        int flag = inspectionItemV1InnerServiceSMOImpl.deleteInspectionItem(inspectionItemPo);
        if (flag < 1) {
            throw new CmdException("删除数据失败");
        }
        InspectionItemTitleDto inspectionItemTitleDto = new InspectionItemTitleDto();
        inspectionItemTitleDto.setItemId(inspectionItemPo.getItemId());
        List<InspectionItemTitleDto> inspectionItemTitleDtos = iInspectionItemTitleV1InnerServiceSMOImpl.queryInspectionItemTitles(inspectionItemTitleDto);
        if (inspectionItemTitleDtos != null && inspectionItemTitleDtos.size() > 0) {
            for (InspectionItemTitleDto inspectionItemTitle : inspectionItemTitleDtos) {
                InspectionItemTitlePo inspectionItemTitlePo = new InspectionItemTitlePo();
                inspectionItemTitlePo.setTitleId(inspectionItemTitle.getTitleId());
                //删除巡检项目下的题目
                int i = iInspectionItemTitleV1InnerServiceSMOImpl.deleteInspectionItemTitle(inspectionItemTitlePo);
                if (i < 1) {
                    throw new CmdException("删除数据失败");
                }
                InspectionItemTitleValueDto inspectionItemTitleValueDto = new InspectionItemTitleValueDto();
                inspectionItemTitleValueDto.setTitleId(inspectionItemTitle.getTitleId());
                List<InspectionItemTitleValueDto> inspectionItemTitleValueDtos = iInspectionItemTitleValueV1InnerServiceSMOImpl.queryInspectionItemTitleValues(inspectionItemTitleValueDto);
                if (inspectionItemTitleValueDtos != null && inspectionItemTitleValueDtos.size() > 0) {
                    for (InspectionItemTitleValueDto inspectionItemTitleValue : inspectionItemTitleValueDtos) {
                        InspectionItemTitleValuePo inspectionItemTitleValuePo = new InspectionItemTitleValuePo();
                        inspectionItemTitleValuePo.setValueId(inspectionItemTitleValue.getValueId());
                        int count = iInspectionItemTitleValueV1InnerServiceSMOImpl.deleteInspectionItemTitleValue(inspectionItemTitleValuePo);
                        if (count < 1) {
                            throw new CmdException("删除数据失败");
                        }
                    }
                }
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
