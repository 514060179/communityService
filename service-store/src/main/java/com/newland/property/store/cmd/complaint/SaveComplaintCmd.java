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
package com.newland.property.store.cmd.complaint;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.dto.complaintEvent.ComplaintEventDto;
import com.newland.property.dto.complaintType.ComplaintTypeDto;
import com.newland.property.dto.data.DatabusDataDto;
import com.newland.property.dto.file.FileDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.job.IDataBusInnerServiceSMO;
import com.newland.property.intf.store.IComplaintEventV1InnerServiceSMO;
import com.newland.property.intf.store.IComplaintTypeV1InnerServiceSMO;
import com.newland.property.intf.store.IComplaintV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.complaint.ComplaintPo;
import com.newland.property.po.complaintEvent.ComplaintEventPo;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：complaint.saveComplaint
 * 请求路劲：/app/complaint.SaveComplaint
 * add by 吴学文 at 2024-02-21 13:08:05 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "complaint.saveComplaint")
public class SaveComplaintCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveComplaintCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeV1InnerServiceSMO complaintTypeV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired
    private IComplaintEventV1InnerServiceSMO complaintEventV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IDataBusInnerServiceSMO dataBusInnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "complaintName", "请求报文中未包含complaintName");
        Assert.hasKeyAndValue(reqJson, "typeCd", "请求报文中未包含typeCd");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "roomId", "请求报文中未包含roomId");
        Assert.hasKeyAndValue(reqJson, "tel", "请求报文中未包含tel");
        Assert.hasKeyAndValue(reqJson, "context", "请求报文中未包含context");


        ComplaintTypeDto complaintTypeDto = new ComplaintTypeDto();
        complaintTypeDto.setTypeCd(reqJson.getString("typeCd"));
        complaintTypeDto.setCommunityId(reqJson.getString("communityId"));
        List<ComplaintTypeDto> complaintTypeDtos = complaintTypeV1InnerServiceSMOImpl.queryComplaintTypes(complaintTypeDto);

        Assert.listOnlyOne(complaintTypeDtos, "投诉类型不存在");


        RoomDto roomDto = new RoomDto();
        roomDto.setRoomId(reqJson.getString("roomId"));
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);

        Assert.listOnlyOne(roomDtos, "房屋不存在");

        reqJson.put("roomName", roomDtos.get(0).getFloorNum() + "-" + roomDtos.get(0).getUnitNum() + "-" + roomDtos.get(0).getRoomNum());

        String ownerId = roomDtos.get(0).getOwnerId();
        if (StringUtil.isEmpty(ownerId)) {
            ownerId = "-1";
        }
        String ownerName = roomDtos.get(0).getOwnerName();
        if (StringUtil.isEmpty(ownerName)) {
            ownerName = "未知";
        }
        reqJson.put("ownerId", ownerId);
        reqJson.put("ownerName", ownerName);

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        String userId = CmdContextUtils.getUserId(cmdDataFlowContext);

        UserDto userDto = new UserDto();
        userDto.setUserId(userId);
        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);

        Assert.listOnlyOne(userDtos, "用户未登录");

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setCommunityId(reqJson.getString("communityId"));
        communityMemberDto.setMemberTypeCd(CommunityMemberDto.MEMBER_TYPE_PROPERTY);
        List<CommunityMemberDto> communityMemberDtos = communityInnerServiceSMOImpl.getCommunityMembers(communityMemberDto);

        Assert.listOnlyOne(communityMemberDtos, "小区未入驻");


        ComplaintPo complaintPo = BeanConvertUtil.covertBean(reqJson, ComplaintPo.class);
        complaintPo.setComplaintId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        complaintPo.setState(ComplaintDto.STATE_WAIT);
        complaintPo.setStoreId(communityMemberDtos.get(0).getMemberId());
        complaintPo.setStartUserId(userId);
        int flag = complaintV1InnerServiceSMOImpl.saveComplaint(complaintPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        ComplaintEventPo complaintEventPo = new ComplaintEventPo();
        complaintEventPo.setRemark("用户提交投诉");
        complaintEventPo.setComplaintId(complaintPo.getComplaintId());
        complaintEventPo.setCommunityId(complaintPo.getCommunityId());
        complaintEventPo.setEventType(ComplaintEventDto.EVENT_TYPE_SUBMIT);
        complaintEventPo.setCreateUserId(userId);
        complaintEventPo.setCreateUserName(userDtos.get(0).getName());
        complaintEventPo.setEventId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        flag = complaintEventV1InnerServiceSMOImpl.saveComplaintEvent(complaintEventPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }

        //todo 图片
        savePhone(reqJson, complaintPo);

        //todo 发送消息给处理师傅
        dataBusInnerServiceSMOImpl.databusData(new DatabusDataDto(DatabusDataDto.BUSINESS_TYPE_SEND_COMPLAINT_NOTIFY_STAFF,BeanConvertUtil.beanCovertJson(complaintPo)));

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }

    /**
     * 保存图片
     *
     * @param reqJson
     * @param complaintPo
     */
    private void savePhone(JSONObject reqJson, ComplaintPo complaintPo) {

        if (!reqJson.containsKey("photos")) {
            return;
        }
        JSONArray photos = reqJson.getJSONArray("photos");
        if (ListUtil.isNull(photos)) {
            return;
        }
        for (int photoIndex = 0; photoIndex < photos.size(); photoIndex++) {
            String _photo = photos.getString(photoIndex);
            if (_photo.length() > 512) {
                FileDto fileDto = new FileDto();
                fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
                fileDto.setFileName(fileDto.getFileId());
                fileDto.setContext(_photo);
                fileDto.setSuffix("jpeg");
                fileDto.setCommunityId(reqJson.getString("communityId"));
                _photo = fileInnerServiceSMOImpl.saveFile(fileDto);
            }
            JSONObject businessUnit = new JSONObject();
            businessUnit.put("fileRelId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
            businessUnit.put("relTypeCd", "13000");
            businessUnit.put("saveWay", "table");
            businessUnit.put("objId", complaintPo.getComplaintId());
            businessUnit.put("fileRealName", _photo);
            businessUnit.put("fileSaveName", _photo);
            FileRelPo fileRelPo = BeanConvertUtil.covertBean(businessUnit, FileRelPo.class);
            fileRelInnerServiceSMOImpl.saveFileRel(fileRelPo);
        }
    }

}
