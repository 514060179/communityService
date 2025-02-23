package com.newland.property.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.data.ExportDataDto;
import com.newland.property.dto.inspection.InspectionTaskDetailDto;
import com.newland.property.dto.repair.RepairReturnVisitDto;
import com.newland.property.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.newland.property.intf.community.IRepairReturnVisitInnerServiceSMO;
import com.newland.property.job.export.IExportDataAdapt;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报修回访导出
 */
@Service("repairReturnVisit")
public class RepairReturnVisitAdapt implements IExportDataAdapt {

    @Autowired
    private IRepairReturnVisitInnerServiceSMO repairReturnVisitInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("报修回访");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("工单编码");
        row.createCell(1).setCellValue("位置");
        row.createCell(2).setCellValue("报修类型");
        row.createCell(3).setCellValue("报修人");
        row.createCell(4).setCellValue("联络方式");
        row.createCell(5).setCellValue("预约时间");

        JSONObject reqJson = exportDataDto.getReqJson();
        RepairReturnVisitDto repairReturnVisitDto = BeanConvertUtil.covertBean(reqJson, RepairReturnVisitDto.class);

        //查询数据
        getRepairReturnVisit(sheet, repairReturnVisitDto);

        return workbook;

    }

    private void getRepairReturnVisit(Sheet sheet, RepairReturnVisitDto repairReturnVisitDto) {
        int count = repairReturnVisitInnerServiceSMOImpl.queryRepairReturnVisitsCount(repairReturnVisitDto);

        double record = Math.ceil((double) count / MAX_ROW);

        if (count < 1) {
            return;
        }
        List<RepairReturnVisitDto> repairReturnVisitDtoList = null;
        for (int page = 1; page <= record; page++) {
            repairReturnVisitDto.setPage(page);
            repairReturnVisitDto.setRow(MAX_ROW);
            repairReturnVisitDtoList = repairReturnVisitInnerServiceSMOImpl.queryRepairReturnVisits(repairReturnVisitDto);
            appendData(repairReturnVisitDtoList, sheet, (page - 1) * MAX_ROW);
        }
    }

    private void appendData(List<RepairReturnVisitDto> repairReturnVisitDtos, Sheet sheet, int step) {

        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < repairReturnVisitDtos.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = JSONObject.parseObject(JSONObject.toJSONString(repairReturnVisitDtos.get(roomIndex)));
            row.createCell(0).setCellValue(dataObj.getString("repairId"));
            row.createCell(1).setCellValue(dataObj.getString("repairObjName"));
            row.createCell(2).setCellValue(dataObj.getString("repairTypeName"));
            row.createCell(3).setCellValue(dataObj.getString("repairName"));
            row.createCell(4).setCellValue(dataObj.getString("tel"));
            row.createCell(5).setCellValue(dataObj.getString("appointmentTime"));
        }
    }
}
