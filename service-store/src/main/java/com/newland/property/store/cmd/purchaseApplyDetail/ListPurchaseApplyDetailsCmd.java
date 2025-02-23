package com.newland.property.store.cmd.purchaseApplyDetail;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.purchase.PurchaseApplyDetailDto;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.api.purchaseApplyDetail.ApiPurchaseApplyDetailDataVo;
import com.newland.property.vo.api.purchaseApplyDetail.ApiPurchaseApplyDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@NewlandPropertyCmd(serviceCode = "purchaseApplyDetail.listPurchaseApplyDetails")
public class ListPurchaseApplyDetailsCmd extends Cmd {

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        super.validatePageInfo(reqJson);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        String storeId = CmdContextUtils.getStoreId(context);
        PurchaseApplyDetailDto purchaseApplyDetailDto = BeanConvertUtil.covertBean(reqJson, PurchaseApplyDetailDto.class);
        purchaseApplyDetailDto.setStoreId(storeId);
        //报修待办查看所有记录权限
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource("/viewAllPurchaseApplyDetail");
        basePrivilegeDto.setUserId(reqJson.getString("userId"));
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        if (privileges == null || privileges.size() == 0) {
            purchaseApplyDetailDto.setCreateUserId(reqJson.getString("userId"));
            purchaseApplyDetailDto.setCreateUserName(reqJson.getString("userName"));
        } else {
            purchaseApplyDetailDto.setCreateUserId("");
            purchaseApplyDetailDto.setCreateUserName("");
        }
        int count = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetailsCount(purchaseApplyDetailDto);
        List<ApiPurchaseApplyDetailDataVo> purchaseApplyDetails = null;
        if (count > 0) {
            purchaseApplyDetails = BeanConvertUtil.covertBeanList(purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto), ApiPurchaseApplyDetailDataVo.class);
        } else {
            purchaseApplyDetails = new ArrayList<>();
        }
        ApiPurchaseApplyDetailVo apiPurchaseApplyDetailVo = new ApiPurchaseApplyDetailVo();
        apiPurchaseApplyDetailVo.setTotal(count);
        apiPurchaseApplyDetailVo.setRecords((int) Math.ceil((double) count / (double) reqJson.getInteger("row")));
        apiPurchaseApplyDetailVo.setPurchaseApplyDetails(purchaseApplyDetails);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(JSONObject.toJSONString(apiPurchaseApplyDetailVo), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }
}
