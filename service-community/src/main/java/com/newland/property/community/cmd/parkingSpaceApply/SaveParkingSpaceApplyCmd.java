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
package com.newland.property.community.cmd.parkingSpaceApply;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.PayFeeDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.parking.ParkingSpaceApplyDto;
import com.newland.property.intf.community.IParkingSpaceApplyV1InnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeV1InnerServiceSMO;
import com.newland.property.intf.user.IBuildingOwnerV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarV1InnerServiceSMO;
import com.newland.property.po.parking.ParkingSpaceApplyPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;

import java.util.List;

/**
 * 类表述：保存
 * 服务编码：parkingSpaceApply.saveParkingSpaceApply
 * 请求路劲：/app/parkingSpaceApply.SaveParkingSpaceApply
 * add by 吴学文 at 2021-10-18 13:00:02 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "parkingSpaceApply.saveParkingSpaceApply")
public class SaveParkingSpaceApplyCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveParkingSpaceApplyCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IParkingSpaceApplyV1InnerServiceSMO parkingSpaceApplyV1InnerServiceSMOImpl;
    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;
    @Autowired
    private IPayFeeV1InnerServiceSMO payFeeV1InnerServiceSMOImpl;
    @Autowired
    private IBuildingOwnerV1InnerServiceSMO buildingOwnerV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "carNum", "请求报文中未包含carNum");
        Assert.hasKeyAndValue(reqJson, "carBrand", "请求报文中未包含carBrand");
        Assert.hasKeyAndValue(reqJson, "carType", "请求报文中未包含carType");
        Assert.hasKeyAndValue(reqJson, "carColor", "请求报文中未包含carColor");
        Assert.hasKeyAndValue(reqJson, "startTime", "请求报文中未包含startTime");
        Assert.hasKeyAndValue(reqJson, "endTime", "请求报文中未包含endTime");
        Assert.hasKeyAndValue(reqJson, "state", "请求报文中未包含state");
        Assert.hasKeyAndValue(reqJson, "applyPersonName", "请求报文中未包含applyPersonName");
        Assert.hasKeyAndValue(reqJson, "applyPersonLink", "请求报文中未包含applyPersonLink");
        Assert.hasKeyAndValue(reqJson, "applyPersonId", "请求报文中未包含applyPersonId");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        ParkingSpaceApplyPo parkingSpaceApplyPo = BeanConvertUtil.covertBean(reqJson, ParkingSpaceApplyPo.class);
        /**
         * 1、車位申請判斷車輛是否存在owner_car，如果存在判断是否有2008001的费用，
         * car_id是费用表中的payer_obj_id。申请的时候判断下owner_id是否为-1，还需到owner表中校验在不在，
         * 2、审核的时，判断车辆是否在owner_car中有，有就跳过。  没有的话写入owner_car，--都要写入pay_fee。
         * 3、支付成功后，回调方法中刷新申请表状态。
         */
        //申请的时候判断下owner_id是否为-1
        if ("-1".equals(parkingSpaceApplyPo.getApplyPersonId())) {
            throw new CmdException("游客身份不能申请车位！");
        }
        //还需到owner表中校验在不在,不是业主则不能申请
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setOwnerId(parkingSpaceApplyPo.getApplyPersonId());
        List<OwnerDto> ownerDtos = buildingOwnerV1InnerServiceSMOImpl.queryBuildingOwners(ownerDto);
        if (ownerDtos == null || ownerDtos.size() < 1) {
            throw new CmdException("不是本小区业主不能申请车位！");
        }
        //判断车辆是否已经有申请单
        ParkingSpaceApplyDto parkingSpaceApplyDto = new ParkingSpaceApplyDto();
        parkingSpaceApplyDto.setCarNum(parkingSpaceApplyPo.getCarNum());
        parkingSpaceApplyDto.setState("1001");//审核中
        int count = parkingSpaceApplyV1InnerServiceSMOImpl.queryParkingSpaceApplysCount(parkingSpaceApplyDto);
        if (count > 1) {
            throw new CmdException("当前车辆申请处理审核中，不能重复申请。");
        }
        //車位申請判斷車輛是否存在owner_car，如果存在判断是否有2008001的费用
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNum(parkingSpaceApplyPo.getCarNum());
        ownerCarDto.setCommunityId(parkingSpaceApplyPo.getCommunityId());
        List<OwnerCarDto> ownerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);
        if (ownerCarDtos != null && ownerCarDtos.size() > 0) {
            PayFeeDto payFeeDto = new PayFeeDto();
            payFeeDto.setPayerObjId(ownerCarDtos.get(0).getCarId());
            payFeeDto.setState("2008001");
            List<PayFeeDto> payFeeDtos = payFeeV1InnerServiceSMOImpl.queryPayFees(payFeeDto);
            if (payFeeDtos != null && payFeeDtos.size() > 0) {
                throw new CmdException("该车辆已经有相关费用，请到停车费中续费。或者联系管理员");
            }
        }

        parkingSpaceApplyPo.setApplyId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        int flag = parkingSpaceApplyV1InnerServiceSMOImpl.saveParkingSpaceApply(parkingSpaceApplyPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
