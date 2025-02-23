package com.newland.property.common.cmd.machineRecord;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.community.CommunityLocationDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.machine.MachineRecordDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.common.IMachineRecordInnerServiceSMO;
import com.newland.property.intf.community.ICommunityLocationInnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.machineRecord.ApiMachineRecordDataVo;
import com.newland.property.vo.api.machineRecord.ApiMachineRecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "machineRecord.listMachineRecords")
public class ListMachineRecordsCmd extends Cmd {

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private ICommunityLocationInnerServiceSMO communityLocationInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含小区信息");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        MachineRecordDto machineRecordDto = BeanConvertUtil.covertBean(reqJson, MachineRecordDto.class);

        int count = machineRecordInnerServiceSMOImpl.queryMachineRecordsCount(machineRecordDto);

        List<ApiMachineRecordDataVo> machineRecords = null;

        if (count > 0) {
            machineRecords = BeanConvertUtil.covertBeanList(machineRecordInnerServiceSMOImpl.queryMachineRecords(machineRecordDto), ApiMachineRecordDataVo.class);
            freshFaceUrl(machineRecords);
            refreshMachineLocation(machineRecords);
        } else {
            machineRecords = new ArrayList<>();
        }

        ApiMachineRecordVo apiMachineRecordVo = new ApiMachineRecordVo();

        apiMachineRecordVo.setTotal(count);
        apiMachineRecordVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiMachineRecordVo.setMachineRecords(machineRecords);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiMachineRecordVo), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }

    private void refreshMachineLocation(List<ApiMachineRecordDataVo> machineRecords) {
        for (ApiMachineRecordDataVo machineDto : machineRecords) {
            getMachineLocation(machineDto);
        }
    }

    private void getMachineLocation(ApiMachineRecordDataVo machineDto) {
        CommunityLocationDto communityLocationDto = new CommunityLocationDto();
        communityLocationDto.setCommunityId(machineDto.getCommunityId());
        communityLocationDto.setLocationId(machineDto.getLocationTypeCd());
        List<CommunityLocationDto> communityLocationDtos = communityLocationInnerServiceSMOImpl.queryCommunityLocations(communityLocationDto);

        if (communityLocationDtos == null || communityLocationDtos.size() < 1) {
            return;
        }

        machineDto.setLocationObjName(communityLocationDtos.get(0).getLocationName());
    }

    private void freshFaceUrl(List<ApiMachineRecordDataVo> machineRecords) {

        List<String> recordIds = new ArrayList<>();

        for (ApiMachineRecordDataVo tmpOwnerDto : machineRecords) {
            recordIds.add(tmpOwnerDto.getMachineRecordId());
        }

        FileRelDto fileRelDto = new FileRelDto();
        //fileRelDto.setObjId(owners.get(0).getMemberId());
        fileRelDto.setObjIds(recordIds.toArray(new String[recordIds.size()]));
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (fileRelDtos == null || fileRelDtos.size() < 1) {
            return ;
        }

        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");

        for (ApiMachineRecordDataVo tmpOwnerDto : machineRecords) {
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                if (!tmpOwnerDto.getMachineRecordId().equals(tmpFileRelDto.getObjId())) {
                    continue;
                }

                if (tmpFileRelDto.getFileSaveName().startsWith("http")) {
                    tmpOwnerDto.setFaceUrl(tmpFileRelDto.getFileSaveName());
                } else {
                    tmpOwnerDto.setFaceUrl(imgUrl + tmpFileRelDto.getFileSaveName());
                }
            }
        }
    }
}
