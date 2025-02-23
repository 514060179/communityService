package com.newland.property.community.cmd.inspectionTaskDetail;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.inspection.InspectionTaskDetailDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailDataVo;
import com.newland.property.vo.api.inspectionTaskDetail.ApiInspectionTaskDetailVo;
import com.newland.property.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "inspectionTaskDetail.listInspectionTaskDetails")
public class ListInspectionTaskDetailsCmd extends Cmd {

    @Autowired
    private IInspectionTaskDetailInnerServiceSMO inspectionTaskDetailInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        InspectionTaskDetailDto inspectionTaskDetailDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDetailDto.class);
        int count = inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetailsCount(inspectionTaskDetailDto);
        List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails = null;
        if (count > 0) {
            inspectionTaskDetails = BeanConvertUtil.covertBeanList(inspectionTaskDetailInnerServiceSMOImpl.queryInspectionTaskDetails(inspectionTaskDetailDto), ApiInspectionTaskDetailDataVo.class);
            refreshPhotos(inspectionTaskDetails);
        } else {
            inspectionTaskDetails = new ArrayList<>();
        }
        ApiInspectionTaskDetailVo apiInspectionTaskDetailVo = new ApiInspectionTaskDetailVo();
        apiInspectionTaskDetailVo.setTotal(count);
        apiInspectionTaskDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiInspectionTaskDetailVo.setInspectionTaskDetails(inspectionTaskDetails);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiInspectionTaskDetailVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void refreshPhotos(List<ApiInspectionTaskDetailDataVo> inspectionTaskDetails) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for (ApiInspectionTaskDetailDataVo inspectionTaskDetail : inspectionTaskDetails) {
            if(!"20200407".equals(inspectionTaskDetail.getState())){
                continue;
            }
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(inspectionTaskDetail.getTaskDetailId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName().startsWith("http")?tmpFileRelDto.getFileRealName():imgUrl+tmpFileRelDto.getFileRealName());
                photoVos.add(photoVo);
            }
            inspectionTaskDetail.setPhotos(photoVos);
        }
    }
}
