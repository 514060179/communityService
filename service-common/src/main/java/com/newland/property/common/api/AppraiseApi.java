package com.newland.property.common.api;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.common.bmo.appraise.IGetAppraiseBMO;
import com.newland.property.common.bmo.appraise.ISaveAppraiseBMO;
import com.newland.property.dto.appraise.AppraiseDto;
import com.newland.property.po.appraise.AppraisePo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/appraise")
public class AppraiseApi {
    @Autowired
    private ISaveAppraiseBMO saveAppraiseBMOImpl;

    @Autowired
    private IGetAppraiseBMO getAppraiseBMOImpl;

    @RequestMapping(value = "/saveAppraise", method = RequestMethod.POST)
    public ResponseEntity<String> saveAppraise(@RequestBody JSONObject reqJson) {
        AppraisePo appraisePo = BeanConvertUtil.covertBean(reqJson, AppraisePo.class);
        return saveAppraiseBMOImpl.saveAppraise(appraisePo);
    }

    @RequestMapping(value = "/getAppraise", method = RequestMethod.GET)
    public ResponseEntity<String> getAppraise(@RequestParam(value = "appraiseId", required = false) String appraiseId,
                                              @RequestParam(value = "objType", required = false) String objType,
                                              @RequestParam(value = "objId", required = false) String objId,
                                              @RequestParam(value = "page") int page,
                                              @RequestParam(value = "row") int row
    ) {
        AppraiseDto appraiseDto = new AppraiseDto();
        appraiseDto.setAppraiseId(appraiseId);
        appraiseDto.setObjType(objType);
        appraiseDto.setObjId(objId);
        appraiseDto.setRow(row);
        appraiseDto.setPage(page);
        return getAppraiseBMOImpl.getAppraise(appraiseDto);
    }
}
