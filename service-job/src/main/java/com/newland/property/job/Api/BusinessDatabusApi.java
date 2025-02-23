package com.newland.property.job.Api;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.system.BusinessDatabusDto;
import com.newland.property.job.bmo.businessDatabus.IDeleteBusinessDatabusBMO;
import com.newland.property.job.bmo.businessDatabus.IGetBusinessDatabusBMO;
import com.newland.property.job.bmo.businessDatabus.ISaveBusinessDatabusBMO;
import com.newland.property.job.bmo.businessDatabus.IUpdateBusinessDatabusBMO;
import com.newland.property.po.business.BusinessDatabusPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/businessDatabus")
public class BusinessDatabusApi {

    @Autowired
    private ISaveBusinessDatabusBMO saveBusinessDatabusBMOImpl;
    @Autowired
    private IUpdateBusinessDatabusBMO updateBusinessDatabusBMOImpl;
    @Autowired
    private IDeleteBusinessDatabusBMO deleteBusinessDatabusBMOImpl;

    @Autowired
    private IGetBusinessDatabusBMO getBusinessDatabusBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /businessDatabus/saveBusinessDatabus
     * @path /app/businessDatabus/saveBusinessDatabus
     */
    @RequestMapping(value = "/saveBusinessDatabus", method = RequestMethod.POST)
    public ResponseEntity<String> saveBusinessDatabus(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "businessTypeCd", "请求报文中未包含businessTypeCd");
        Assert.hasKeyAndValue(reqJson, "beanName", "请求报文中未包含beanName");


        BusinessDatabusPo businessDatabusPo = BeanConvertUtil.covertBean(reqJson, BusinessDatabusPo.class);
        return saveBusinessDatabusBMOImpl.save(businessDatabusPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /businessDatabus/updateBusinessDatabus
     * @path /app/businessDatabus/updateBusinessDatabus
     */
    @RequestMapping(value = "/updateBusinessDatabus", method = RequestMethod.POST)
    public ResponseEntity<String> updateBusinessDatabus(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "businessTypeCd", "请求报文中未包含businessTypeCd");
        Assert.hasKeyAndValue(reqJson, "beanName", "请求报文中未包含beanName");
        Assert.hasKeyAndValue(reqJson, "databusId", "databusId不能为空");


        BusinessDatabusPo businessDatabusPo = BeanConvertUtil.covertBean(reqJson, BusinessDatabusPo.class);
        return updateBusinessDatabusBMOImpl.update(businessDatabusPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /businessDatabus/deleteBusinessDatabus
     * @path /app/businessDatabus/deleteBusinessDatabus
     */
    @RequestMapping(value = "/deleteBusinessDatabus", method = RequestMethod.POST)
    public ResponseEntity<String> deleteBusinessDatabus(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "databusId", "databusId不能为空");


        BusinessDatabusPo businessDatabusPo = BeanConvertUtil.covertBean(reqJson, BusinessDatabusPo.class);
        return deleteBusinessDatabusBMOImpl.delete(businessDatabusPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param businessTypeCd 小区ID
     * @return
     * @serviceCode /businessDatabus/queryBusinessDatabus
     * @path /app/businessDatabus/queryBusinessDatabus
     */
    @RequestMapping(value = "/queryBusinessDatabus", method = RequestMethod.GET)
    public ResponseEntity<String> queryBusinessDatabus(@RequestParam(value = "businessTypeCd", required = false) String businessTypeCd,
                                                       @RequestParam(value = "beanName", required = false) String beanName,
                                                       @RequestParam(value = "databusId", required = false) String databusId,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        BusinessDatabusDto businessDatabusDto = new BusinessDatabusDto();
        businessDatabusDto.setPage(page);
        businessDatabusDto.setRow(row);
        businessDatabusDto.setBusinessTypeCd(businessTypeCd);
        businessDatabusDto.setBeanName(beanName);
        businessDatabusDto.setDatabusId(databusId);
        return getBusinessDatabusBMOImpl.get(businessDatabusDto);
    }
}
