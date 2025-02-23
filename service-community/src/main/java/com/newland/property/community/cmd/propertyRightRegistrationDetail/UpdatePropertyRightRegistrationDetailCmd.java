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
package com.newland.property.community.cmd.propertyRightRegistrationDetail;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IPropertyRightRegistrationDetailV1InnerServiceSMO;
import com.newland.property.intf.community.IPropertyRightRegistrationV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.room.PropertyRightRegistrationPo;
import com.newland.property.po.room.PropertyRightRegistrationDetailPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * 类表述：更新
 * 服务编码：propertyRightRegistrationDetail.updatePropertyRightRegistrationDetail
 * 请求路劲：/app/propertyRightRegistrationDetail.UpdatePropertyRightRegistrationDetail
 * add by 吴学文 at 2021-10-11 09:15:52 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "propertyRightRegistrationDetail.updatePropertyRightRegistrationDetail")
public class UpdatePropertyRightRegistrationDetailCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(UpdatePropertyRightRegistrationDetailCmd.class);

    @Autowired
    private IPropertyRightRegistrationDetailV1InnerServiceSMO propertyRightRegistrationDetailV1InnerServiceSMOImpl;

    @Autowired
    private IPropertyRightRegistrationV1InnerServiceSMO propertyRightRegistrationV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "prrId", "prrId不能为空");
        Assert.hasKeyAndValue(reqJson, "communityId", "communityId不能为空");
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        //获取材料类型(001身份证 002购房合同 003维修基金 004契税)
        String securities = reqJson.getString("securities");
        //获取身份证照片信息
        if ("001".equals(securities) && reqJson.containsKey("idCardPhotos") && !StringUtil.isEmpty(reqJson.getString("idCardPhotos"))) {
            String idCardPhotos = reqJson.getString("idCardPhotos");
            JSONArray objects = JSONArray.parseArray(idCardPhotos);
            if (objects.size() != 2 || "[]".equals(idCardPhotos)) {
                throw new CmdException("请上传身份证正反两张照片！");
            }
        }
        //获取购房合同图片信息
        if ("002".equals(securities) && reqJson.containsKey("housePurchasePhotos") && !StringUtil.isEmpty(reqJson.getString("housePurchasePhotos"))) {
            String housePurchasePhotos = reqJson.getString("housePurchasePhotos");
            if (housePurchasePhotos.length() < 1 || "[]".equals(housePurchasePhotos)) {
                throw new CmdException("购房合同图片不能为空！");
            }
        }
        //获取维修基金标识
        if ("003".equals(securities) && reqJson.containsKey("isTrue") && "true".equals(reqJson.getString("isTrue"))) {
            String repairPhotos = reqJson.getString("repairPhotos");
            if (repairPhotos.length() < 1 || "[]".equals(repairPhotos)) {
                throw new CmdException("维修基金图片不能为空！");
            }
        } else if ("003".equals(securities) && reqJson.containsKey("isTrue") && "false".equals(reqJson.getString("isTrue"))) {//当维修基金为空时，维修基金图片为空
            reqJson.put("repairPhotos", null);
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(reqJson.getString("prrId"));
            fileRelDto.setRelTypeCd("26000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                for (FileRelDto file : fileRelDtos) {
                    FileRelPo fileRel = new FileRelPo();
                    fileRel.setFileRelId(file.getFileRelId());
                    fileRelInnerServiceSMOImpl.deleteFileRel(fileRel);
                }
            }
        }
        //获取契税标识
        if ("004".equals(securities) && reqJson.containsKey("isTrue") && "true".equals(reqJson.getString("isTrue"))) {
            String deedTaxPhotos = reqJson.getString("deedTaxPhotos");
            if (deedTaxPhotos.length() < 1 || "[]".equals(deedTaxPhotos)) {
                throw new CmdException("契税证明图片不能为空！");
            }
        } else if ("004".equals(securities) && reqJson.containsKey("isTrue") && "false".equals(reqJson.getString("isTrue"))) {//当契税为空时，契税图片为空
            reqJson.put("deedTaxPhotos", null);
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(reqJson.getString("prrId"));
            fileRelDto.setRelTypeCd("27000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                for (FileRelDto file : fileRelDtos) {
                    FileRelPo fileRel = new FileRelPo();
                    fileRel.setFileRelId(file.getFileRelId());
                    fileRelInnerServiceSMOImpl.deleteFileRel(fileRel);
                }
            }
        }
        PropertyRightRegistrationDetailPo propertyRightRegistrationDetailPo = BeanConvertUtil.covertBean(reqJson, PropertyRightRegistrationDetailPo.class);
        int flag = propertyRightRegistrationDetailV1InnerServiceSMOImpl.updatePropertyRightRegistrationDetail(propertyRightRegistrationDetailPo);

        if (flag < 1) {
            throw new CmdException("更新详情数据失败");
        }
        //产权登记详情修改成功后，产权登记状态要变为未审核状态
        PropertyRightRegistrationPo propertyRightRegistrationPo = new PropertyRightRegistrationPo();
        propertyRightRegistrationPo.setPrrId(reqJson.getString("prrId"));
        propertyRightRegistrationPo.setState("0");
        int newFlag = propertyRightRegistrationV1InnerServiceSMOImpl.updatePropertyRightRegistration(propertyRightRegistrationPo);
        if (newFlag < 1) {
            throw new CmdException("更新数据失败");
        }
        FileRelPo fileRelPo = new FileRelPo();
        fileRelPo.setObjId(propertyRightRegistrationDetailPo.getPrrId());
        //table表示表存储 ftp表示ftp文件存储
        fileRelPo.setSaveWay("ftp");
        fileRelPo.setCreateTime(new Date());
        //身份证图片上传
        List<String> idCardPhotos = propertyRightRegistrationDetailPo.getIdCardPhotos();
        if (idCardPhotos != null && idCardPhotos.size() > 0) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(propertyRightRegistrationDetailPo.getPrrId());
            fileRelDto.setRelTypeCd("60000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                for (FileRelDto file : fileRelDtos) {
                    FileRelPo fileRel = new FileRelPo();
                    fileRel.setFileRelId(file.getFileRelId());
                    fileRelInnerServiceSMOImpl.deleteFileRel(fileRel);
                }
            }
            //60000表示身份证图片
            fileRelPo.setRelTypeCd("60000");
            for (String photo : idCardPhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
        }
        //购房合同照片上传
        List<String> housePurchasePhotos = propertyRightRegistrationDetailPo.getHousePurchasePhotos();
        if (housePurchasePhotos != null && housePurchasePhotos.size() > 0) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(propertyRightRegistrationDetailPo.getPrrId());
            fileRelDto.setRelTypeCd("25000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                for (FileRelDto file : fileRelDtos) {
                    FileRelPo fileRel = new FileRelPo();
                    fileRel.setFileRelId(file.getFileRelId());
                    fileRelInnerServiceSMOImpl.deleteFileRel(fileRel);
                }
            }
            //25000表示购房合同图片
            fileRelPo.setRelTypeCd("25000");
            for (String photo : housePurchasePhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
        }
        //维修基金图片上传
        List<String> repairPhotos = propertyRightRegistrationDetailPo.getRepairPhotos();
        if (repairPhotos != null && repairPhotos.size() > 0) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(propertyRightRegistrationDetailPo.getPrrId());
            fileRelDto.setRelTypeCd("26000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                for (FileRelDto file : fileRelDtos) {
                    FileRelPo fileRel = new FileRelPo();
                    fileRel.setFileRelId(file.getFileRelId());
                    fileRelInnerServiceSMOImpl.deleteFileRel(fileRel);
                }
            }
            //26000表示维修基金图片
            fileRelPo.setRelTypeCd("26000");
            for (String photo : repairPhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
        }
        //契税证明图片上传
        List<String> deedTaxPhotos = propertyRightRegistrationDetailPo.getDeedTaxPhotos();
        if (deedTaxPhotos != null && deedTaxPhotos.size() > 0) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(propertyRightRegistrationDetailPo.getPrrId());
            fileRelDto.setRelTypeCd("27000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                for (FileRelDto file : fileRelDtos) {
                    FileRelPo fileRel = new FileRelPo();
                    fileRel.setFileRelId(file.getFileRelId());
                    fileRelInnerServiceSMOImpl.deleteFileRel(fileRel);
                }
            }
            //27000表示契税图片
            fileRelPo.setRelTypeCd("27000");
            for (String photo : deedTaxPhotos) {
                fileRelPo.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
                fileRelPo.setFileRealName(photo);
                fileRelPo.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
            }
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
