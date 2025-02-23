package com.newland.property.job.importData;

import com.newland.property.dto.data.ExportDataDto;
import com.newland.property.dto.data.ImportDataDto;
import com.newland.property.dto.log.AssetImportLogDetailDto;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.util.List;

/**
 * 导入数据适配器
 */
public interface IImportDataAdapt {


    /**
     * 导入数据
     * @param assetImportLogDetailDtos
     */
    void importData(List<AssetImportLogDetailDto> assetImportLogDetailDtos);
}
