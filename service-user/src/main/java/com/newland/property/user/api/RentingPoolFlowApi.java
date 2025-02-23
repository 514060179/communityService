package com.newland.property.user.api;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.renting.RentingPoolFlowDto;
import com.newland.property.po.renting.RentingPoolFlowPo;
import com.newland.property.user.bmo.rentingPoolFlow.IDeleteRentingPoolFlowBMO;
import com.newland.property.user.bmo.rentingPoolFlow.IGetRentingPoolFlowBMO;
import com.newland.property.user.bmo.rentingPoolFlow.ISaveRentingPoolFlowBMO;
import com.newland.property.user.bmo.rentingPoolFlow.IUpdateRentingPoolFlowBMO;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "/rentingPoolFlow")
public class RentingPoolFlowApi {

    @Autowired
    private ISaveRentingPoolFlowBMO saveRentingPoolFlowBMOImpl;
    @Autowired
    private IUpdateRentingPoolFlowBMO updateRentingPoolFlowBMOImpl;
    @Autowired
    private IDeleteRentingPoolFlowBMO deleteRentingPoolFlowBMOImpl;

    @Autowired
    private IGetRentingPoolFlowBMO getRentingPoolFlowBMOImpl;

    /**
     * 微信保存消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingPoolFlow/saveRentingPoolFlow
     * @path /app/rentingPoolFlow/saveRentingPoolFlow
     */
    @RequestMapping(value = "/saveRentingPoolFlow", method = RequestMethod.POST)
    public ResponseEntity<String> saveRentingPoolFlow(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "rentingId", "请求报文中未包含rentingId");
        Assert.hasKeyAndValue(reqJson, "useName", "请求报文中未包含useName");
        Assert.hasKeyAndValue(reqJson, "useTel", "请求报文中未包含useTel");
        Assert.hasKeyAndValue(reqJson, "dealTime", "请求报文中未包含dealTime");
        Assert.hasKeyAndValue(reqJson, "userRole", "请求报文中未包含userRole");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");


        RentingPoolFlowPo rentingPoolFlowPo = BeanConvertUtil.covertBean(reqJson, RentingPoolFlowPo.class);
        return saveRentingPoolFlowBMOImpl.save(rentingPoolFlowPo);
    }

    /**
     * 微信修改消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingPoolFlow/updateRentingPoolFlow
     * @path /app/rentingPoolFlow/updateRentingPoolFlow
     */
    @RequestMapping(value = "/updateRentingPoolFlow", method = RequestMethod.POST)
    public ResponseEntity<String> updateRentingPoolFlow(@RequestBody JSONObject reqJson) {

        Assert.hasKeyAndValue(reqJson, "rentingId", "请求报文中未包含rentingId");
        Assert.hasKeyAndValue(reqJson, "useName", "请求报文中未包含useName");
        Assert.hasKeyAndValue(reqJson, "useTel", "请求报文中未包含useTel");
        Assert.hasKeyAndValue(reqJson, "dealTime", "请求报文中未包含dealTime");
        Assert.hasKeyAndValue(reqJson, "userRole", "请求报文中未包含userRole");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "flowId", "flowId不能为空");


        RentingPoolFlowPo rentingPoolFlowPo = BeanConvertUtil.covertBean(reqJson, RentingPoolFlowPo.class);
        return updateRentingPoolFlowBMOImpl.update(rentingPoolFlowPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param reqJson
     * @return
     * @serviceCode /rentingPoolFlow/deleteRentingPoolFlow
     * @path /app/rentingPoolFlow/deleteRentingPoolFlow
     */
    @RequestMapping(value = "/deleteRentingPoolFlow", method = RequestMethod.POST)
    public ResponseEntity<String> deleteRentingPoolFlow(@RequestBody JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "communityId", "小区ID不能为空");

        Assert.hasKeyAndValue(reqJson, "flowId", "flowId不能为空");


        RentingPoolFlowPo rentingPoolFlowPo = BeanConvertUtil.covertBean(reqJson, RentingPoolFlowPo.class);
        return deleteRentingPoolFlowBMOImpl.delete(rentingPoolFlowPo);
    }

    /**
     * 微信删除消息模板
     *
     * @param communityId 小区ID
     * @returnaddOwner
     * @serviceCode /rentingPoolFlow/queryRentingPoolFlow
     * @path /app/rentingPoolFlow/queryRentingPoolFlow
     */
    @RequestMapping(value = "/queryRentingPoolFlow", method = RequestMethod.GET)
    public ResponseEntity<String> queryRentingPoolFlow(@RequestParam(value = "communityId") String communityId,
                                                       @RequestParam(value = "rentingId") String rentingId,
                                                       @RequestParam(value = "state", required = false) String state,
                                                       @RequestParam(value = "page") int page,
                                                       @RequestParam(value = "row") int row) {
        RentingPoolFlowDto rentingPoolFlowDto = new RentingPoolFlowDto();
        rentingPoolFlowDto.setPage(page);
        rentingPoolFlowDto.setRow(row);
        rentingPoolFlowDto.setRentingId(rentingId);
        rentingPoolFlowDto.setState(state);
        rentingPoolFlowDto.setCommunityId(communityId);
        return getRentingPoolFlowBMOImpl.get(rentingPoolFlowDto);
    }
}
