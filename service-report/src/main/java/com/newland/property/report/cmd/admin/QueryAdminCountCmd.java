package com.newland.property.report.cmd.admin;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.dto.inspection.InspectionTaskDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.repair.RepairDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.shop.ShopDto;
import com.newland.property.dto.store.StoreDto;
import com.newland.property.intf.community.*;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.mall.IShopInnerServiceSMO;
import com.newland.property.intf.store.IComplaintInnerServiceSMO;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "admin.queryAdminCount")
public class QueryAdminCountCmd extends Cmd {


    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private ICommunityInnerServiceSMO communityInnerServiceSMOImpl;

    @Autowired(required = false)
    private IShopInnerServiceSMO shopInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    @Autowired
    private IRepairInnerServiceSMO repairInnerServiceSMOImpl;

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    @Autowired
    private IComplaintInnerServiceSMO complaintInnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = CmdContextUtils.getStoreId(context);

        StoreDto storeDto = new StoreDto();
        storeDto.setStoreId(storeId);
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_ADMIN);
        int count = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        if (count < 1) {
            throw new CmdException("非法操作，请用系统管理员账户操作");
        }
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {

        List<Map> datas = new ArrayList<>();
        //todo 小区个数

        CommunityDto communityDto = new CommunityDto();
        int communityCount = communityInnerServiceSMOImpl.queryCommunitysCount(communityDto);
        setDatas(datas, "小区数", communityCount);

        //todo 物业个数
        StoreDto storeDto = new StoreDto();
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_PROPERTY);
        int storeCount = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        setDatas(datas, "物业数", storeCount);

        //todo 商家个数
        storeDto = new StoreDto();
        storeDto.setStoreTypeCd(StoreDto.STORE_TYPE_MALL);
        storeCount = storeInnerServiceSMOImpl.getStoreCount(storeDto);
        setDatas(datas, "商家数", storeCount);

        // todo 商铺个数
        int shopCount = 0;
        if ("ON".equals(MappingCache.getValue("HAS_HC_MALL"))) {
            ShopDto shopDto = new ShopDto();
            shopCount = shopInnerServiceSMOImpl.queryShopsCount(shopDto);
        }
        setDatas(datas, "店铺数", shopCount);

        //todo 房屋数
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomType(RoomDto.ROOM_TYPE_ROOM);
        int roomCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        setDatas(datas, "房屋数", roomCount);

        //todo 商铺数
        roomDto = new RoomDto();
        roomDto.setRoomType(RoomDto.ROOM_TYPE_SHOPS);
        roomCount = roomV1InnerServiceSMOImpl.queryRoomsCount(roomDto);
        setDatas(datas, "商铺数", roomCount);

        //todo 业主数
        OwnerDto ownerDto = new OwnerDto();
        int ownerCount = ownerV1InnerServiceSMOImpl.queryOwnersCount(ownerDto);
        setDatas(datas, "业主数", ownerCount);

        //todo 车辆数

        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setLeaseTypes(new String[]{OwnerCarDto.LEASE_TYPE_INNER,
                OwnerCarDto.LEASE_TYPE_MONTH,
                OwnerCarDto.LEASE_TYPE_SALE,
                OwnerCarDto.LEASE_TYPE_NO_MONEY,
                OwnerCarDto.LEASE_TYPE_RESERVE
        });
        int carCount = ownerCarV1InnerServiceSMOImpl.queryOwnerCarsCount(ownerCarDto);
        setDatas(datas, "车辆数", carCount);

        String startTime = DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_B);
        String endTime = startTime + " 23:59:59";
        //todo 缴费订单数

        FeeDetailDto feeDetailDto = new FeeDetailDto();
        feeDetailDto.setState(FeeDetailDto.STATE_NORMAL);
        feeDetailDto.setStartTime(DateUtil.getDateFromStringB(startTime));
        feeDetailDto.setEndTime(DateUtil.getDateFromStringA(endTime));
        int feeCount = feeDetailInnerServiceSMOImpl.queryFeeDetailsCount(feeDetailDto);
        setDatas(datas, "今日缴费数", feeCount);

        //todo 报修订单数
        RepairDto repairDto = new RepairDto();
        repairDto.setStartTime(startTime);
        repairDto.setEndTime(endTime);
        int repairCount = repairInnerServiceSMOImpl.queryRepairsCount(repairDto);
        setDatas(datas, "今日报修数", repairCount);

        //todo 巡检订单数
        InspectionTaskDto inspectionTaskDto = new InspectionTaskDto();
        inspectionTaskDto.setStartTime(startTime);
        inspectionTaskDto.setEndTime(endTime);
        inspectionTaskDto.setStates(new String[]{InspectionTaskDto.STATE_DOING, InspectionTaskDto.STATE_FINISH});
        int inspectionCount = inspectionTaskInnerServiceSMOImpl.queryInspectionTasksCount(inspectionTaskDto);
        setDatas(datas, "今日巡检数", inspectionCount);

        //todo 投诉单
        ComplaintDto complaintDto = new ComplaintDto();
        complaintDto.setStartTime(startTime);
        complaintDto.setEndTime(endTime);
        int complaintCount = complaintInnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        setDatas(datas, "今日投诉数", complaintCount);



        context.setResponseEntity(ResultVo.createResponseEntity(datas));

    }

    private void setDatas(List<Map> datas, String name, int value) {
        Map info = new HashMap();
        info.put("name", name);
        info.put("value", value);
        datas.add(info);
    }
}
