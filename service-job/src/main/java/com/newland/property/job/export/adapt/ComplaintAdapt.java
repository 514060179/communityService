package com.newland.property.job.export.adapt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.complaint.ComplaintDto;
import com.newland.property.dto.complaintTypeUser.ComplaintTypeUserDto;
import com.newland.property.dto.data.ExportDataDto;
import com.newland.property.dto.repair.RepairReturnVisitDto;
import com.newland.property.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.newland.property.intf.store.IComplaintTypeUserV1InnerServiceSMO;
import com.newland.property.intf.store.IComplaintV1InnerServiceSMO;
import com.newland.property.job.export.IExportDataAdapt;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 投诉建议导出
 */
@Service("complaint")
public class ComplaintAdapt implements IExportDataAdapt {

    @Autowired
    private IComplaintV1InnerServiceSMO complaintV1InnerServiceSMOImpl;


    @Autowired
    private IComplaintTypeUserV1InnerServiceSMO complaintTypeUserV1InnerServiceSMOImpl;
    private static final int MAX_ROW = 60000;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        //工作簿
        SXSSFWorkbook workbook = null;
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("投诉建议表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("订单编码");
        row.createCell(1).setCellValue("类型");
        row.createCell(2).setCellValue("房屋");
        row.createCell(3).setCellValue("联系人");
        row.createCell(4).setCellValue("联系电话");
        row.createCell(5).setCellValue("状态");
        row.createCell(6).setCellValue("处理人");
        row.createCell(7).setCellValue("创建时间");

        JSONObject reqJson = exportDataDto.getReqJson();
        ComplaintDto complaintDto = BeanConvertUtil.covertBean(reqJson, ComplaintDto.class);

        //查询数据
        getComplaint(sheet, complaintDto);

        return workbook;

    }

    private void getComplaint(Sheet sheet, ComplaintDto complaintDto) {
        int count = complaintV1InnerServiceSMOImpl.queryComplaintsCount(complaintDto);

        double record = Math.ceil((double) count / MAX_ROW);

        if (count < 1) {
            return;
        }
        List<ComplaintDto> complaintDtoList = null;
        for (int page = 1; page <= record; page++) {
            complaintDto.setPage(page);
            complaintDto.setRow(MAX_ROW);
            complaintDtoList = complaintV1InnerServiceSMOImpl.queryComplaints(complaintDto);
            // 查询类型员工
            toQueryStaff(complaintDtoList);
            appendData(complaintDtoList, sheet, (page - 1) * MAX_ROW);
        }
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

    private void appendData(List<ComplaintDto> complaintDtoList, Sheet sheet, int step) {

        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < complaintDtoList.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = JSONObject.parseObject(JSON.toJSONStringWithDateFormat(complaintDtoList.get(roomIndex), "yyyy-MM-dd HH:mm:ss"));
            row.createCell(0).setCellValue(dataObj.getString("complaintId"));
            row.createCell(1).setCellValue(dataObj.getString("typeName"));
            row.createCell(2).setCellValue(dataObj.getString("roomName"));
            row.createCell(3).setCellValue(dataObj.getString("complaintName"));
            row.createCell(4).setCellValue(dataObj.getString("tel"));
            row.createCell(5).setCellValue(dataObj.getString("stateName"));
            row.createCell(6).setCellValue(handleStaffs(dataObj.getJSONArray("staffs")));
            row.createCell(7).setCellValue(dataObj.getString("createTime"));
        }
    }

    private String handleStaffs(JSONArray staffs) {
        StringBuffer returnStaffs = new StringBuffer("---");
        if (staffs != null && staffs.size() >= 1) {
            returnStaffs = new StringBuffer();
            for (int i = 0; i < staffs.size(); i++) {
                JSONObject staff = staffs.getJSONObject(i);
                returnStaffs.append(staff.getString("staffName")).append("、");
            }
        }
        return returnStaffs.toString().substring(0, returnStaffs.length() - 1);
    }
}
