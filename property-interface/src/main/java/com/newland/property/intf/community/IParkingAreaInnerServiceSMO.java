package com.newland.property.intf.community;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.parking.ParkingAreaDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IParkingAreaInnerServiceSMO
 * @Description 停车场接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "community-service", configuration = {FeignConfiguration.class})
@RequestMapping("/parkingAreaApi")
public interface IParkingAreaInnerServiceSMO {

    /**
     * 获取完整的停车场停息
     * @param areaNum
     * @return
     */
    public ParkingAreaDto getFullParkAreaInfo(String areaNum);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param parkingAreaDto 数据对象分享
     * @return ParkingAreaDto 对象数据
     */
    @RequestMapping(value = "/queryParkingAreas", method = RequestMethod.POST)
    List<ParkingAreaDto> queryParkingAreas(@RequestBody ParkingAreaDto parkingAreaDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param parkingAreaDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryParkingAreasCount", method = RequestMethod.POST)
    int queryParkingAreasCount(@RequestBody ParkingAreaDto parkingAreaDto);
}
