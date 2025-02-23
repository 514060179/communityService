/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.job.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.data.ImportDataDto;
import com.newland.property.intf.job.IUserImportDataV1InnerServiceSMO;
import com.newland.property.job.importData.ImportDataQueue;
import com.newland.property.po.log.AssetImportLogPo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * excel 数据导入处理类
 */
@RestController
public class UserImportDataV1InnerServiceSMOImpl extends BaseServiceSMO implements IUserImportDataV1InnerServiceSMO {


    /**
     * excel数据导入处理
     * @param assetImportLogPo
     * @return
     */
    @Override
    public int importExcelData(@RequestBody AssetImportLogPo assetImportLogPo) {
        ImportDataDto importDataDto = new ImportDataDto();
        importDataDto.setLogId(assetImportLogPo.getLogId());
        importDataDto.setBusinessAdapt(assetImportLogPo.getLogType());
        importDataDto.setCommunityId(assetImportLogPo.getCommunityId());
        ImportDataQueue.addMsg(importDataDto);
        return 1;
    }
}
