package com.newland.property.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.attendance.AttendanceClassesStaffDto;
import com.newland.property.dto.community.CommunityMemberDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.machine.MachineTranslateDto;
import com.newland.property.dto.system.Business;
import com.newland.property.dto.user.UserDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.common.IMachineTranslateInnerServiceSMO;
import com.newland.property.intf.community.ICommunityMemberV1InnerServiceSMO;
import com.newland.property.intf.store.IStoreUserV1InnerServiceSMO;
import com.newland.property.intf.user.IAttendanceClassesStaffV1InnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.job.adapt.hcIotNew.http.ISendIot;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.utils.util.DateUtil;
import com.newland.property.utils.util.ImageUtils;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 200100060001
 * 200100070001
 */
@Component(value = "sendAttendanceStaffDataToIotAdapt")
public class SendAttendanceStaffDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Autowired
    private ISendIot sendIotImpl;

    @Autowired
    private IStoreUserV1InnerServiceSMO storeUserV1InnerServiceSMOImpl;

    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private ICommunityMemberV1InnerServiceSMO communityMemberV1InnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IAttendanceClassesStaffV1InnerServiceSMO attendanceClassesStaffV1InnerServiceSMOImpl;


    @Override
    public void execute(Business business, List<Business> businesses) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

        JSONObject data = business.getData();
        String csId = data.getString("csId");
        if (StringUtil.isEmpty(csId)) {
            throw new IllegalArgumentException("推送考勤员工参数错误");
        }

        AttendanceClassesStaffDto attendanceClassesStaffDto = new AttendanceClassesStaffDto();
        attendanceClassesStaffDto.setCsId(csId);
        List<AttendanceClassesStaffDto> attendanceClassesStaffs = attendanceClassesStaffV1InnerServiceSMOImpl.queryAttendanceClassesStaffs(attendanceClassesStaffDto);
        if (ListUtil.isNull(attendanceClassesStaffs)) {
            return;
        }


        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(csId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);

        if (ListUtil.isNull(fileRelDtos)) {
            return;
        }
        String imgUrl = MappingCache.getValue(MappingConstant.FILE_DOMAIN, "IMG_PATH");

        if (fileRelDtos.get(0).getFileSaveName().startsWith("http")) {
            attendanceClassesStaffDto.setPersonFace(fileRelDtos.get(0).getFileSaveName());
        } else {
            attendanceClassesStaffDto.setPersonFace(imgUrl + fileRelDtos.get(0).getFileSaveName());
        }

        String faceBase64 = ImageUtils.getBase64ByImgUrl(attendanceClassesStaffDto.getPersonFace());


        JSONObject staff = new JSONObject();
        staff.put("classesId", attendanceClassesStaffs.get(0).getClassesId());
        staff.put("staffId", attendanceClassesStaffs.get(0).getStaffId());
        staff.put("staffPhoto", faceBase64);


        ResultVo resultVo = sendIotImpl.post("/iot/api/staff.addAttendanceStaffApi", staff);

        CommunityMemberDto communityMemberDto = new CommunityMemberDto();
        communityMemberDto.setMemberId(attendanceClassesStaffs.get(0).getStoreId());
        List<CommunityMemberDto> communityMemberDtos = communityMemberV1InnerServiceSMOImpl.queryCommunityMembers(communityMemberDto);

        if (ListUtil.isNull(communityMemberDtos)) {
            return;
        }

        UserDto userDto = new UserDto();
        userDto.setUserId(attendanceClassesStaffs.get(0).getStaffId());

        List<UserDto> userDtos = userV1InnerServiceSMOImpl.queryUsers(userDto);
        if (ListUtil.isNull(userDtos)) {
            return;
        }

        if (resultVo.getCode() != ResultVo.CODE_OK) {
            saveTranslateLog(communityMemberDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_ADD_ATTENDANCE_CLASSES,
                    userDtos.get(0).getUserId(), userDtos.get(0).getName(),
                    MachineTranslateDto.STATE_ERROR, resultVo.getMsg());
            return;
        }

        saveTranslateLog(communityMemberDtos.get(0).getCommunityId(), MachineTranslateDto.CMD_ADD_ATTENDANCE_CLASSES,
                userDtos.get(0).getUserId(), userDtos.get(0).getName(),
                MachineTranslateDto.STATE_SUCCESS, resultVo.getMsg());
    }

    /**
     * 存储交互 记录
     *
     * @param communityId
     */
    public void saveTranslateLog(String communityId, String cmd, String objId, String objName, String state, String remark) {
        MachineTranslateDto machineTranslateDto = new MachineTranslateDto();
        machineTranslateDto.setMachineTranslateId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_machineTranslateId));
        machineTranslateDto.setCommunityId(communityId);
        machineTranslateDto.setMachineCmd(cmd);
        machineTranslateDto.setMachineCode("-1");
        machineTranslateDto.setMachineId("-1");
        machineTranslateDto.setObjId(objId);
        machineTranslateDto.setObjName(objName);
        machineTranslateDto.setTypeCd(MachineTranslateDto.TYPE_ATTENDANCE);
        machineTranslateDto.setRemark(remark);
        machineTranslateDto.setState(state);
        machineTranslateDto.setbId("-1");
        machineTranslateDto.setObjBId("-1");
        machineTranslateDto.setUpdateTime(DateUtil.getNow(DateUtil.DATE_FORMATE_STRING_A));
        machineTranslateInnerServiceSMOImpl.saveMachineTranslate(machineTranslateDto);
    }

}
