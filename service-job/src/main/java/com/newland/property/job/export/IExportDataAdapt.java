package com.newland.property.job.export;

import com.newland.property.dto.data.ExportDataDto;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.text.ParseException;

/**
 * 导出数据适配器
 */
public interface IExportDataAdapt {

    /**
     * 导出数据 excel
     * @param exportDataDto
     */
    SXSSFWorkbook exportData(ExportDataDto exportDataDto) throws ParseException;
}
