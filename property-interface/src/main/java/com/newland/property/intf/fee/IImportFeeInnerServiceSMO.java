package com.newland.property.intf.fee;

import com.newland.property.config.feign.FeignConfiguration;
import com.newland.property.dto.importData.ImportFeeDto;
import com.newland.property.po.importFee.ImportFeePo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @ClassName IImportFeeInnerServiceSMO
 * @Description 费用导入接口类
 * @Author wuxw
 * @Date 2019/4/24 9:04
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@FeignClient(name = "fee-service", configuration = {FeignConfiguration.class})
@RequestMapping("/importFeeApi")
public interface IImportFeeInnerServiceSMO {


    @RequestMapping(value = "/saveImportFee", method = RequestMethod.POST)
    public int saveImportFee(@RequestBody ImportFeePo importFeePo);

    @RequestMapping(value = "/updateImportFee", method = RequestMethod.POST)
    public int updateImportFee(@RequestBody ImportFeePo importFeePo);

    @RequestMapping(value = "/deleteImportFee", method = RequestMethod.POST)
    public int deleteImportFee(@RequestBody ImportFeePo importFeePo);

    /**
     * <p>查询小区楼信息</p>
     *
     * @param importFeeDto 数据对象分享
     * @return ImportFeeDto 对象数据
     */
    @RequestMapping(value = "/queryImportFees", method = RequestMethod.POST)
    List<ImportFeeDto> queryImportFees(@RequestBody ImportFeeDto importFeeDto);

    /**
     * 查询<p>小区楼</p>总记录数
     *
     * @param importFeeDto 数据对象分享
     * @return 小区下的小区楼记录数
     */
    @RequestMapping(value = "/queryImportFeesCount", method = RequestMethod.POST)
    int queryImportFeesCount(@RequestBody ImportFeeDto importFeeDto);
}
