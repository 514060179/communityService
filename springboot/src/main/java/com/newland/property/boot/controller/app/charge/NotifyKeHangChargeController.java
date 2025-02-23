package com.newland.property.boot.controller.app.charge;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.base.controller.BaseController;
import com.newland.property.core.log.LoggerFactory;
import com.newland.property.dto.charge.NotifyChargeOrderDto;
import com.newland.property.intf.common.INotifyChargeV1InnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 科航充电桩 充电完成回调
 *
 */
@RestController
@RequestMapping(path = "/app/charge/kehang")
public class NotifyKeHangChargeController extends BaseController {

    private final static Logger logger = LoggerFactory.getLogger(NotifyKeHangChargeController.class);


    private static final String FINISH_CHARGE = "net.equip.charge.slow.async.notice.finish";

    @Autowired
    private INotifyChargeV1InnerServiceSMO notifyChargeV1InnerServiceSMOImpl;

    /**
     * <p>支付回调Api</p>
     *
     * @param request
     * @throws Exception
     */
    @RequestMapping(path = "/notice", method = RequestMethod.POST)
    public ResponseEntity<String> notice(
            @RequestBody String postInfo,
            HttpServletRequest request) {

        String api = request.getHeader("api");

        JSONObject reqJson = JSONObject.parseObject(postInfo);
        if(FINISH_CHARGE.equals(api)){
            NotifyChargeOrderDto notifyChargeOrderDto = new NotifyChargeOrderDto();
            notifyChargeOrderDto.setMachineCode(reqJson.getString("equipCd"));
            notifyChargeOrderDto.setPortCode(reqJson.getString("port"));
            notifyChargeOrderDto.setBodyParam(postInfo);
            notifyChargeOrderDto.setReason(reqJson.getString("reason"));
            ResultVo resultVo = notifyChargeV1InnerServiceSMOImpl.finishCharge(notifyChargeOrderDto);

            if (resultVo.getCode() != ResultVo.CODE_OK) {
               return new ResponseEntity<>("FAIL",HttpStatus.OK);
            }

        }

        return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
    }



}
