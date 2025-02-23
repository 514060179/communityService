package com.newland.property.job.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.dto.importData.ImportMeterMachineDto;
import com.newland.property.dto.log.AssetImportLogDetailDto;
import com.newland.property.dto.meter.MeterTypeDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.intf.common.IMeterMachineSpecV1InnerServiceSMO;
import com.newland.property.intf.common.IMeterMachineV1InnerServiceSMO;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.fee.IMeterTypeV1InnerServiceSMO;
import com.newland.property.job.importData.DefaultImportData;
import com.newland.property.job.importData.IImportDataAdapt;
import com.newland.property.po.meter.MeterMachinePo;
import com.newland.property.po.meter.MeterMachineSpecPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("importMeterMachineQueueData")
public class ImportMeterMachineQueueDataAdapt extends DefaultImportData implements IImportDataAdapt {

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IMeterMachineV1InnerServiceSMO meterMachineV1InnerServiceSMOImpl;

    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IMeterMachineSpecV1InnerServiceSMO meterMachineSpecV1InnerServiceSMOImpl;

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IMeterTypeV1InnerServiceSMO meterTypeV1InnerServiceSMOImpl;

    @Override
    public void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos) {
        for (AssetImportLogDetailDto assetImportLogDetailDto : assetImportLogDetailDtos) {

            try {
                doImportData(assetImportLogDetailDto);
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId());
            } catch (Exception e) {
                e.printStackTrace();
                updateImportLogDetailState(assetImportLogDetailDto.getDetailId(), e);
            }
        }
    }

    /**
     * 导入数据
     *
     * @param assetImportLogDetailDto
     */
    private void doImportData(AssetImportLogDetailDto assetImportLogDetailDto) {
        JSONObject data = JSONObject.parseObject(assetImportLogDetailDto.getContent());
        ImportMeterMachineDto importMeterMachineDto = BeanConvertUtil.covertBean(data, ImportMeterMachineDto.class);
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setFeeName(importMeterMachineDto.getFeeName());
        feeConfigDto.setCommunityId(importMeterMachineDto.getCommunityId());
        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);
        Assert.listOnlyOne(feeConfigDtos, "费用项不存在或存在多条");

        String[] roomItems = importMeterMachineDto.getRoomName().split("-", 3);
        //查询房屋
        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(roomItems[0]);
        roomDto.setUnitNum(roomItems[1]);
        roomDto.setRoomNum(roomItems[2]);
        roomDto.setCommunityId(importMeterMachineDto.getCommunityId());
        List<RoomDto> roomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "房屋不存在");

        MeterTypeDto meterTypeDto = new MeterTypeDto();
        meterTypeDto.setCommunityId(importMeterMachineDto.getCommunityId());
        meterTypeDto.setTypeName(importMeterMachineDto.getMeterType());
        List<MeterTypeDto> meterTypeDtos = meterTypeV1InnerServiceSMOImpl.queryMeterTypes(meterTypeDto);

        Assert.listOnlyOne(meterTypeDtos, "房屋不存在");


        MeterMachinePo meterMachinePo = BeanConvertUtil.covertBean(importMeterMachineDto, MeterMachinePo.class);
        meterMachinePo.setMachineId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        meterMachinePo.setFeeConfigName(feeConfigDtos.get(0).getFeeName());
        meterMachinePo.setCurDegrees("0");
        meterMachinePo.setCurReadingTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        meterMachinePo.setPrestoreDegrees("0");
        meterMachinePo.setHeartbeatTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        meterMachinePo.setFeeConfigId(feeConfigDtos.get(0).getConfigId());
        meterMachinePo.setReadDay(1);
        meterMachinePo.setReadHours(1);
        meterMachinePo.setRoomId(roomDtos.get(0).getRoomId());
        meterMachinePo.setMeterType(meterTypeDtos.get(0).getTypeId());

        int flag = meterMachineV1InnerServiceSMOImpl.saveMeterMachine(meterMachinePo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        if (StringUtil.isEmpty(importMeterMachineDto.getValue1())) {
            return;
        }

        MeterMachineSpecPo meterMachineSpecPo = null;
        meterMachineSpecPo = new MeterMachineSpecPo();
        meterMachineSpecPo.setMachineId(meterMachinePo.getMachineId());
        meterMachineSpecPo.setSpecId("120101");
        meterMachineSpecPo.setSpecName("采集器ID");
        meterMachineSpecPo.setSpecValue(importMeterMachineDto.getValue1());
        meterMachineSpecPo.setMmsId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        meterMachineSpecPo.setCommunityId(meterMachinePo.getCommunityId());
        flag = meterMachineSpecV1InnerServiceSMOImpl.saveMeterMachineSpec(meterMachineSpecPo);

        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }


    }
}
