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
package com.newland.property.acct.cmd.invoice;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.invoiceApply.InvoiceApplyDto;
import com.newland.property.dto.invoiceEvent.InvoiceEventDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.acct.IInvoiceApplyV1InnerServiceSMO;
import com.newland.property.intf.acct.IInvoiceEventV1InnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.invoiceApply.InvoiceApplyPo;
import com.newland.property.po.invoiceEvent.InvoiceEventPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * 类表述：上传发票
 * 服务编码：invoiceApply.updateInvoiceApply
 * 请求路劲：/app/invoiceApply.UpdateInvoiceApply
 * add by 吴学文 at 2023-10-08 16:26:34 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "invoice.uploadInvoicePhoto")
public class UploadInvoicePhotoCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UploadInvoicePhotoCmd.class);


    @Autowired
    private IInvoiceApplyV1InnerServiceSMO invoiceApplyV1InnerServiceSMOImpl;

    @Autowired
    private IInvoiceEventV1InnerServiceSMO invoiceEventV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "applyId", "applyId不能为空");
        Assert.hasKeyAndValue(reqJson, "invoiceCode", "发票号不能为空");

        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");


    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        InvoiceApplyDto invoiceApplyDto = new InvoiceApplyDto();
        invoiceApplyDto.setApplyId(reqJson.getString("applyId"));
        invoiceApplyDto.setCommunityId(reqJson.getString("communityId"));
        List<InvoiceApplyDto> invoiceApplyDtos = invoiceApplyV1InnerServiceSMOImpl.queryInvoiceApplys(invoiceApplyDto);

        Assert.listOnlyOne(invoiceApplyDtos, "发票申请不存在");


        //todo 修改发票号
        InvoiceApplyPo invoiceApplyPo = new InvoiceApplyPo();
        invoiceApplyPo.setApplyId(reqJson.getString("applyId"));
        invoiceApplyPo.setInvoiceCode(reqJson.getString("invoiceCode"));
        if (InvoiceApplyDto.STATE_UPLOAD.equals(invoiceApplyDtos.get(0).getState())) {
            invoiceApplyPo.setState(InvoiceApplyDto.STATE_GET);
        }
        int flag = invoiceApplyV1InnerServiceSMOImpl.updateInvoiceApply(invoiceApplyPo);

        if (flag < 1) {
            throw new CmdException("更新数据失败");
        }

        // todo 保存事件
        InvoiceEventPo invoiceEventPo = new InvoiceEventPo();
        invoiceEventPo.setApplyId(reqJson.getString("applyId"));
        invoiceEventPo.setCommunityId(reqJson.getString("communityId"));
        invoiceEventPo.setEventId(GenerateCodeFactory.getGeneratorId("11"));
        invoiceEventPo.setEventType(InvoiceEventDto.STATE_UPLOAD);
        invoiceEventPo.setCreateUserId(userId);
        invoiceEventPo.setCreateUserName(userDtos.get(0).getName());
        invoiceEventPo.setRemark("上传发票，发票号：" + reqJson.getString("invoiceCode"));
        invoiceEventV1InnerServiceSMOImpl.saveInvoiceEvent(invoiceEventPo);

        //todo 保存图片
        if (!reqJson.containsKey("photos")) {
            return;
        }

        JSONArray photos = reqJson.getJSONArray("photos");

        if (photos == null || photos.isEmpty()) {
            return;
        }

        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setObjId(reqJson.getString("applyId"));
        fileRelPo.setRelTypeCd(FileRelDto.REL_TYPE_CE_INVOICE);
        fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);

        FileRelPo tmpFileRelPo = null;
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            tmpFileRelPo = new FileRelPo();
            tmpFileRelPo.setObjId(reqJson.getString("applyId"));
            tmpFileRelPo.setRelTypeCd(FileRelDto.REL_TYPE_CE_INVOICE);
            tmpFileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_fileRelId));
            tmpFileRelPo.setSaveWay("OSS");
            tmpFileRelPo.setFileRealName(photos.getString(photoIndex));
            tmpFileRelPo.setFileSaveName(photos.getString(photoIndex));
            fileRelInnerServiceSMOImpl.saveFileRel(tmpFileRelPo);
        }


        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

}
