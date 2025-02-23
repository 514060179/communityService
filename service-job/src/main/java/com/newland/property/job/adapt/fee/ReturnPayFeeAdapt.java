package com.newland.property.job.adapt.fee;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.owner.OwnerRoomRelDto;
import com.newland.property.dto.wechat.SmallWeChatDto;
import com.newland.property.dto.user.UserDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.community.ICommunityInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.order.IPrivilegeInnerServiceSMO;
import com.newland.property.intf.store.ISmallWeChatInnerServiceSMO;
import com.newland.property.intf.store.ISmallWechatAttrInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IOwnerRoomRelInnerServiceSMO;
import com.newland.property.intf.user.IStaffAppAuthInnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.msgNotify.MsgNotifyFactory;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.StringUtil;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 退费申请通知适配器
 *
 * @author fqz
 * @Date 2021-08-19 10:12
 */
@Component(value = "returnPayFeeAdapt")
public class ReturnPayFeeAdapt extends DatabusAdaptImpl {

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IOwnerRoomRelInnerServiceSMO ownerRoomRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMO;

    @Autowired
    private ISmallWechatAttrInnerServiceSMO smallWechatAttrInnerServiceSMOImpl;

    @Autowired
    private ISmallWeChatInnerServiceSMO smallWeChatInnerServiceSMOImpl;

    @Autowired
    private IPrivilegeInnerServiceSMO privilegeInnerServiceSMO;

    @Autowired
    private IStaffAppAuthInnerServiceSMO staffAppAuthInnerServiceSMO;

    @Autowired
    private RestTemplate outRestTemplate;


    private static Logger logger = LoggerFactory.getLogger(ReturnPayFeeAdapt.class);

    //模板信息推送地址
    private static String sendMsgUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";

    @Override
    public void execute(Business business, List<Business> businesses) {
        JSONObject data = business.getData();
        JSONArray jsonArray = data.getJSONArray("ReturnPayFeePo");
        JSONObject param = jsonArray.getJSONObject(0);
        //查询小区信息
        CommunityDto communityDto = new CommunityDto();
        communityDto.setCommunityId(param.getString("communityId"));
        List<CommunityDto> communityDtos = communityInnerServiceSMO.queryCommunitys(communityDto);
        Assert.listOnlyOne(communityDtos, "查询小区错误！");
        //获取费用详情id
        String detailId = param.getString("detailId");
        //获取费用id
        String feeId = param.getString("feeId");
        FeeDto feeDto = new FeeDto();
        feeDto.setFeeId(feeId);
        //根据费用id查询费用
        List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
        Assert.listOnlyOne(feeDtos, "查询费用错误！");
        //获取付费对象id
        String payerObjId = feeDtos.get(0).getPayerObjId();
        String ownerId = "";
        //获取付费对象类型
        String payerObjType = feeDtos.get(0).getPayerObjType();
        if (!StringUtil.isEmpty(payerObjType) && "3333".equals(payerObjType)) { //房屋
            OwnerRoomRelDto ownerRoomRelDto = new OwnerRoomRelDto();
            ownerRoomRelDto.setRoomId(payerObjId);
            List<OwnerRoomRelDto> ownerRoomRelDtos = ownerRoomRelInnerServiceSMOImpl.queryOwnerRoomRels(ownerRoomRelDto);
            Assert.listOnlyOne(ownerRoomRelDtos, "查询业主房屋关系错误！");
            //获取业主id
            ownerId = ownerRoomRelDtos.get(0).getOwnerId();
        } else if (!StringUtil.isEmpty(payerObjType) && "6666".equals(payerObjType)) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setMemberId(payerObjId);
            ownerCarDto.setCarTypeCd("1001"); //业主车辆
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
            Assert.listOnlyOne(ownerCarDtos, "查询业主车辆错误！");
            ownerId = ownerCarDtos.get(0).getOwnerId();
        }
        //根据业主id查询业主
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(ownerId);
        List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        Assert.listOnlyOne(ownerDtos, "查询业主信息错误！");
        //获取业主姓名
        String name = ownerDtos.get(0).getName();
        JSONObject paramIn = new JSONObject();
        paramIn.put("detailId", detailId);
        paramIn.put("ownerId", ownerId);
        paramIn.put("name", name);
        publishMsg(paramIn, communityDtos.get(0));
    }

    /**
     * 退费申请给审批员工推送信息
     *
     * @param paramIn
     * @param communityDto
     */
    private void publishMsg(JSONObject paramIn, CommunityDto communityDto) {
        //查询公众号配置
        SmallWeChatDto smallWeChatDto = new SmallWeChatDto();
        smallWeChatDto.setWeChatType("1100");
        smallWeChatDto.setObjType(SmallWeChatDto.OBJ_TYPE_COMMUNITY);
        smallWeChatDto.setObjId(communityDto.getCommunityId());
        List<SmallWeChatDto> smallWeChatDtos = smallWeChatInnerServiceSMOImpl.querySmallWeChats(smallWeChatDto);
        if (smallWeChatDto == null || smallWeChatDtos.size() <= 0) {
            logger.info("未配置微信公众号信息,定时任务执行结束");
            return;
        }

        // 根据特定权限查询 有该权限的 员工
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/admin.html#/pages/property/returnPayFeeManage");
        List<UserDto> userDtos = privilegeInnerServiceSMO.queryPrivilegeUsers(basePrivilegeDto);
        for (UserDto userDto : userDtos) {
            MsgNotifyFactory.sendApplyReturnFeeMsg(communityDto.getCommunityId(), userDto.getUserId(), paramIn);
        }
    }

}
