package com.newland.property.store.cmd.complaint;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.dto.complaintTypeUser.ComplaintTypeUserDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.intf.common.IComplaintUserInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.store.IComplaintTypeUserV1InnerServiceSMO;
import com.newland.property.intf.store.IComplaintV1InnerServiceSMO;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.vo.ResultVo;
import com.newland.property.vo.api.junkRequirement.PhotoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "complaint.listComplaints")
public class ListComplaintsCmd extends Cmd {

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IComplaintUserInnerServiceSMO complaintUserInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;


    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "communityId", "必填，请填写小区信息");
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {

        ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);
        int count = complaintV1InnerServiceSMOImpl.queryComplaintsCount(complaintDto);
        List<ComplaintDto> complaintDtos = null;
        if (count > 0) {
            complaintDtos = complaintV1InnerServiceSMOImpl.queryComplaints(complaintDto);
            refreshPhotos(complaintDtos);
        } else {
            complaintDtos = new ArrayList<>();
        }

        //todo 查询类型员工
        toQueryStaff(complaintDtos);

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, complaintDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        context.setResponseEntity(responseEntity);
    }


    private void toQueryStaff(List<ComplaintDto> complaintDtos) {

        if (ListUtil.isNull(complaintDtos)) {
            return;
        }

        List<String> typeCds = new ArrayList<>();
        for (ComplaintDto complaintDto : complaintDtos) {
            typeCds.add(complaintDto.getTypeCd());
        }

        if (ListUtil.isNull(typeCds)) {
            return;
        }

        ComplaintTypeUserDto complaintTypeUserDto = new ComplaintTypeUserDto();
        complaintTypeUserDto.setTypeCds(typeCds.toArray(new String[typeCds.size()]));

        List<ComplaintTypeUserDto> complaintTypeUserDtos = complaintTypeUserV1InnerServiceSMOImpl.queryComplaintTypeUsers(complaintTypeUserDto);

        if (ListUtil.isNull(complaintTypeUserDtos)) {
            return;
        }
        List<ComplaintTypeUserDto> staffs = null;
        for (ComplaintDto complaintDto : complaintDtos) {
            staffs = new ArrayList<>();
            if (ComplaintDto.STATE_FINISH.equals(complaintDto.getState())) {
                continue;
            }
            for (ComplaintTypeUserDto complaintTypeUserDto1 : complaintTypeUserDtos) {
                if (complaintDto.getTypeCd().equals(complaintTypeUserDto1.getTypeCd())) {
                    staffs.add(complaintTypeUserDto1);
                }
            }
            complaintDto.setStaffs(staffs);
        }

    }


    private void refreshPhotos(List<ComplaintDto> complaints) {
        List<PhotoVo> photoVos = null;
        PhotoVo photoVo = null;
        for (ComplaintDto complaintDataVo : complaints) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(complaintDataVo.getComplaintId());
            fileRelDto.setRelTypeCd("13000");
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            photoVos = new ArrayList<>();
            for (FileRelDto tmpFileRelDto : fileRelDtos) {
                photoVo = new PhotoVo();
                photoVo.setUrl(tmpFileRelDto.getFileRealName());
                photoVos.add(photoVo);
            }
            complaintDataVo.setPhotos(photoVos);
        }
    }
}
