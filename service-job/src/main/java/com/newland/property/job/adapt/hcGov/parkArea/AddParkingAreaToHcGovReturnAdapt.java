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
package com.newland.property.job.adapt.hcGov.parkArea;

import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.hcGovTranslate.HcGovTranslateDto;
import com.newland.property.dto.parking.ParkingAreaAttrDto;
import com.newland.property.dto.reportData.ReportDataDto;
import com.newland.property.intf.common.IHcGovTranslateInnerServiceSMO;
import com.newland.property.intf.community.IParkingAreaAttrInnerServiceSMO;
import com.newland.property.job.adapt.hcGov.HcGovConstant;
import com.newland.property.job.adapt.hcGov.IReportReturnDataAdapt;
import com.newland.property.po.parking.ParkingAreaAttrPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 新增楼栋同步HC政务接口 返回
 * <p>
 * 接口协议地址： https://gitee.com/java110/microCommunityInformation/tree/master/info-doc#1%E6%A5%BC%E6%A0%8B%E4%B8%8A%E4%BC%A0
 *
 * @desc add by 吴学文 16:20
 */
@Component(value = "ADD_PARKING_AREA_RETURN")
public class AddParkingAreaToHcGovReturnAdapt implements IReportReturnDataAdapt {

    @Autowired
    private IParkingAreaAttrInnerServiceSMO parkingAreaAttrInnerServiceSMOImpl;
    @Autowired
    private IHcGovTranslateInnerServiceSMO hcGovTranslateInnerServiceSMOImpl;

    @Override
    public void reportReturn(ReportDataDto reportDataDto, String extCommunityId) {

        HcGovTranslateDto hcGovTranslateDto = new HcGovTranslateDto();
        hcGovTranslateDto.setTranId(reportDataDto.getReportDataHeaderDto().getTranId());
        hcGovTranslateDto.setServiceCode(reportDataDto.getReportDataHeaderDto().getServiceCode());
        List<HcGovTranslateDto> hcGovTranslateDtos = hcGovTranslateInnerServiceSMOImpl.queryHcGovTranslates(hcGovTranslateDto);
        if (hcGovTranslateDtos == null || hcGovTranslateDtos.size() < 1) {
            throw new IllegalArgumentException("查询推送报文失败。不是同一订单信息");
        }

        ParkingAreaAttrDto parkingAreaAttrDto = new ParkingAreaAttrDto();
        parkingAreaAttrDto.setPaId(hcGovTranslateDtos.get(0).getObjId());
        parkingAreaAttrDto.setCommunityId(hcGovTranslateDtos.get(0).getCommunityId());
        parkingAreaAttrDto.setSpecCd( HcGovConstant.EXT_COMMUNITY_ID);
        List<ParkingAreaAttrDto> parkingAreaAttrDtos = parkingAreaAttrInnerServiceSMOImpl.queryParkingAreaAttrs(parkingAreaAttrDto);

        ParkingAreaAttrPo parkingAreaAttrPo = new ParkingAreaAttrPo();
        parkingAreaAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_floorId));
        parkingAreaAttrPo.setPaId(parkingAreaAttrDto.getPaId());
        parkingAreaAttrPo.setCommunityId(parkingAreaAttrDto.getCommunityId());
        parkingAreaAttrPo.setSpecCd(parkingAreaAttrDto.getSpecCd());
        parkingAreaAttrPo.setValue(reportDataDto.getReportDataBodyDto().getString("extPaId"));
        if (parkingAreaAttrDtos == null || parkingAreaAttrDtos.size() < 1) {
            int flag = parkingAreaAttrInnerServiceSMOImpl.saveParkingAreaAttr(parkingAreaAttrPo);
            if (flag < 1) {
                throw new IllegalArgumentException("保存楼栋属性失败");
            }
        }


    }
}
