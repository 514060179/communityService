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
package com.newland.property.intf.job;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.user.UserDownloadFileDto;
import com.newland.property.po.log.AssetImportLogPo;
import com.newland.property.po.user.UserDownloadFilePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * excel 数据导入 处理类
 */
@FeignClient(name = "job-service", configuration = {FeignConfiguration.class})
@RequestMapping("/userImportDataV1Api")
public interface IUserImportDataV1InnerServiceSMO {


    @RequestMapping(value = "/importExcelData", method = RequestMethod.POST)
    int importExcelData(@RequestBody AssetImportLogPo assetImportLogPo);

}
