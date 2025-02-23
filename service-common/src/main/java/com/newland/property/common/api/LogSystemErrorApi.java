package com.newland.property.common.api;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.common.bmo.logSystemError.IDeleteLogSystemErrorBMO;
import com.newland.property.common.bmo.logSystemError.IGetLogSystemErrorBMO;
import com.newland.property.common.bmo.logSystemError.ISaveLogSystemErrorBMO;
import com.newland.property.common.bmo.logSystemError.IUpdateLogSystemErrorBMO;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/logSystemError")
public class LogSystemErrorApi {

    @Autowired
    private ISaveLogSystemErrorBMO saveLogSystemErrorBMOImpl;
    @Autowired
    private IUpdateLogSystemErrorBMO updateLogSystemErrorBMOImpl;
    @Autowired
    private IDeleteLogSystemErrorBMO deleteLogSystemErrorBMOImpl;

    @Autowired
    private IGetLogSystemErrorBMO getLogSystemErrorBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /logSystemError/saveLogSystemError
     * @path /app/logSystemError/saveLogSystemError
     */
    @RequestMapping(value = "/saveLogSystemError", method = RequestMethod.POST)
    public ResponseEntity<String> saveLogSystemError(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "errType", "请求报文中未包含errType");


        LogSystemErrorPo logSystemErrorPo = BeanConvertUtil.covertBean(reqJson, LogSystemErrorPo.class);
        return saveLogSystemErrorBMOImpl.save(logSystemErrorPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /logSystemError/updateLogSystemError
     * @path /app/logSystemError/updateLogSystemError
     */
    @RequestMapping(value = "/updateLogSystemError", method = RequestMethod.POST)
    public ResponseEntity<String> updateLogSystemError(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "errType", "请求报文中未包含errType");
        Assert.hasKeyAndValue(reqJson, "errId", "errId不能为空");


        LogSystemErrorPo logSystemErrorPo = BeanConvertUtil.covertBean(reqJson, LogSystemErrorPo.class);
        return updateLogSystemErrorBMOImpl.update(logSystemErrorPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /logSystemError/deleteLogSystemError
     * @path /app/logSystemError/deleteLogSystemError
     */
    @RequestMapping(value = "/deleteLogSystemError", method = RequestMethod.POST)
    public ResponseEntity<String> deleteLogSystemError(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "errId", "errId不能为空");


        LogSystemErrorPo logSystemErrorPo = BeanConvertUtil.covertBean(reqJson, LogSystemErrorPo.class);
        return deleteLogSystemErrorBMOImpl.delete(logSystemErrorPo);
    }

    /**
     * 微信删除消息模板
     *
     * @return
     * @serviceCode /logSystemError/queryLogSystemError
     * @path /app/logSystemError/queryLogSystemError
     */
    @RequestMapping(value = "/queryLogSystemError", method = RequestMethod.GET)
    public ResponseEntity<String> queryLogSystemError(
            @RequestParam(value = "page") int page,
            @RequestParam(value = "row") int row) {
        LogSystemErrorDto logSystemErrorDto = new LogSystemErrorDto();
        logSystemErrorDto.setPage(page);
        logSystemErrorDto.setRow(row);
        return getLogSystemErrorBMOImpl.get(logSystemErrorDto);
    }
}
