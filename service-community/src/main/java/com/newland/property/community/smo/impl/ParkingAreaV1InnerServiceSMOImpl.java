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
package com.newland.property.community.smo.impl;


import com.newland.property.community.dao.IParkingAreaV1ServiceDao;
import com.newland.property.dto.parking.ParkingAreaAttrDto;
import com.newland.property.dto.parking.ParkingAreaDto;
import com.newland.property.intf.community.IParkingAreaV1InnerServiceSMO;
import com.newland.property.po.parking.ParkingAreaPo;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类表述： 服务之前调用的接口实现类，不对外提供接口能力 只用于接口建调用
 * add by 吴学文 at 2022-04-08 09:22:00 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@RestController
public class ParkingAreaV1InnerServiceSMOImpl extends BaseServiceSMO implements IParkingAreaV1InnerServiceSMO {

    @Autowired
    private IParkingAreaV1ServiceDao parkingAreaV1ServiceDaoImpl;


    @Override
    public int saveParkingArea(@RequestBody ParkingAreaPo parkingAreaPo) {
        int saveFlag = parkingAreaV1ServiceDaoImpl.saveParkingAreaInfo(BeanConvertUtil.beanCovertMap(parkingAreaPo));
        return saveFlag;
    }

     @Override
    public int updateParkingArea(@RequestBody  ParkingAreaPo parkingAreaPo) {
        int saveFlag = parkingAreaV1ServiceDaoImpl.updateParkingAreaInfo(BeanConvertUtil.beanCovertMap(parkingAreaPo));
        return saveFlag;
    }

     @Override
    public int deleteParkingArea(@RequestBody  ParkingAreaPo parkingAreaPo) {
       parkingAreaPo.setStatusCd("1");
       int saveFlag = parkingAreaV1ServiceDaoImpl.updateParkingAreaInfo(BeanConvertUtil.beanCovertMap(parkingAreaPo));
       return saveFlag;
    }

    @Override
    public List<ParkingAreaDto> queryParkingAreas(@RequestBody  ParkingAreaDto parkingAreaDto) {

        //校验是否传了 分页信息

        int page = parkingAreaDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            parkingAreaDto.setPage((page - 1) * parkingAreaDto.getRow());
        }

        List<ParkingAreaDto> parkingAreas = BeanConvertUtil.covertBeanList(parkingAreaV1ServiceDaoImpl.getParkingAreaInfo(BeanConvertUtil.beanCovertMap(parkingAreaDto)), ParkingAreaDto.class);
        freshParkingAreas(parkingAreas);
        return parkingAreas;
    }

    private void freshParkingAreas(List<ParkingAreaDto> parkingAreas) {

        List<String> paIds = new ArrayList<>();

        if (parkingAreas == null || parkingAreas.size() < 1) {
            return;
        }
        for (ParkingAreaDto parkingArea : parkingAreas) {
            paIds.add(parkingArea.getPaId());
        }

        Map info = new HashMap();
        info.put("paIds", paIds.toArray(new String[paIds.size()]));
        List<ParkingAreaAttrDto> parkingAreaAttrDtos = BeanConvertUtil.covertBeanList(parkingAreaV1ServiceDaoImpl.getParkingAreaAttrInfo(info), ParkingAreaAttrDto.class);

        if (parkingAreaAttrDtos == null || parkingAreaAttrDtos.size() < 1) {
            return;
        }
        for (ParkingAreaDto parkingArea : parkingAreas) {
            List<ParkingAreaAttrDto> tmpParkingAreaAttrDtos = new ArrayList<>();
            for (ParkingAreaAttrDto parkingAreaAttrDto : parkingAreaAttrDtos) {
                if (parkingArea.getPaId().equals(parkingAreaAttrDto.getPaId())) {
                    tmpParkingAreaAttrDtos.add(parkingAreaAttrDto);
                }
            }
            parkingArea.setAttrs(tmpParkingAreaAttrDtos);
        }
    }

    @Override
    public int queryParkingAreasCount(@RequestBody ParkingAreaDto parkingAreaDto) {
        return parkingAreaV1ServiceDaoImpl.queryParkingAreasCount(BeanConvertUtil.beanCovertMap(parkingAreaDto));    }

}
