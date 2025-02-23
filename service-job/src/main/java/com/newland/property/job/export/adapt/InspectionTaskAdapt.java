package com.newland.property.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.data.ExportDataDto;
import com.newland.property.dto.inspection.InspectionTaskDetailDto;
import com.newland.property.dto.inspection.InspectionTaskDto;
import com.newland.property.intf.community.IInspectionTaskDetailInnerServiceSMO;
import com.newland.property.intf.community.IInspectionTaskInnerServiceSMO;
import com.newland.property.job.export.IExportDataAdapt;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.api.inspectionTask.ApiInspectionTaskDataVo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 巡检任务导出
 */
@Service("inspectionTask")
public class InspectionTaskAdapt implements IExportDataAdapt {

    @Autowired
    private IInspectionTaskInnerServiceSMO inspectionTaskInnerServiceSMOImpl;

    private static final int MAX_ROW = 60000;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {

        SXSSFWorkbook workbook = null;  //工作簿
        String userId = "";
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);

        Sheet sheet = workbook.createSheet("巡检任务");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("任务编码");
        row.createCell(1).setCellValue("巡检计划");
        row.createCell(2).setCellValue("巡检人\n开始/结束时间");
        row.createCell(3).setCellValue("实际巡检时间");
        row.createCell(4).setCellValue("计划巡检人");
        row.createCell(5).setCellValue("当前巡检人");
        row.createCell(6).setCellValue("转移描述");
        row.createCell(7).setCellValue("巡检方式");
        row.createCell(8).setCellValue("巡检状态");


        JSONObject reqJson = exportDataDto.getReqJson();
        InspectionTaskDto inspectionTaskDto = BeanConvertUtil.covertBean(reqJson, InspectionTaskDto.class);
        if (reqJson.containsKey("moreState") && reqJson.getString("moreState").contains(",")) {
            inspectionTaskDto.setStates(reqJson.getString("moreState").split(","));
        }

        //查询当天巡检任务标识
        if (!StringUtil.isEmpty(reqJson.getString("isToday"))) {
            inspectionTaskDto.setDayTask(reqJson.getString("isToday"));
        }
        //查询数据
        getInspectionTask(sheet, inspectionTaskDto);

        return workbook;

    }

    private void getInspectionTask(Sheet sheet, InspectionTaskDto inspectionTaskDto) {
        int count = inspectionTaskInnerServiceSMOImpl.queryInspectionTasksCount(inspectionTaskDto);

        double record = Math.ceil((double) count / MAX_ROW);

        if (count < 1) {
            return;
        }
        List<ApiInspectionTaskDataVo> inspectionTasks = null;
        for (int page = 1; page <= record; page++) {
            inspectionTaskDto.setPage(page);
            inspectionTaskDto.setRow(MAX_ROW);
            inspectionTasks = BeanConvertUtil.covertBeanList(inspectionTaskInnerServiceSMOImpl.queryInspectionTasks(inspectionTaskDto), ApiInspectionTaskDataVo.class);
            appendData(inspectionTasks, sheet, (page - 1) * MAX_ROW);
        }
    }

    private void appendData(List<ApiInspectionTaskDataVo> inspectionTask, Sheet sheet, int step) {

        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < inspectionTask.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = JSONObject.parseObject(JSONObject.toJSONString(inspectionTask.get(roomIndex)));
            row.createCell(0).setCellValue(dataObj.getString("taskId"));
            row.createCell(1).setCellValue(dataObj.getString("inspectionPlanName"));
            row.createCell(2).setCellValue(dataObj.getString("planInsTime")+"\n"+dataObj.getString("planEndTime"));
            row.createCell(3).setCellValue(dataObj.getString("actInsTime"));
            row.createCell(4).setCellValue(dataObj.getString("originalPlanUserName"));
            row.createCell(5).setCellValue(dataObj.getString("planUserName"));
            row.createCell(6).setCellValue(dataObj.getString("transferDesc"));
            row.createCell(7).setCellValue(dataObj.getString("signTypeName"));
            row.createCell(8).setCellValue(dataObj.getString("stateName"));
        }
    }
}
