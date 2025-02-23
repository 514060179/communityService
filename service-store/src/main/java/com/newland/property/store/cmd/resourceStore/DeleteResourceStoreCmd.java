package com.newland.property.store.cmd.resourceStore;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.purchase.AllocationStorehouseDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.purchase.PurchaseApplyDetailDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.newland.property.intf.store.IPurchaseApplyDetailInnerServiceSMO;
import com.newland.property.intf.store.IResourceStoreInnerServiceSMO;
import com.newland.property.intf.store.IResourceStoreV1InnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.po.purchase.ResourceStorePo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@NewlandPropertyCmd(serviceCode = "resourceStore.deleteResourceStore")
public class DeleteResourceStoreCmd extends Cmd {

    @Autowired
    private IResourceStoreInnerServiceSMO resourceStoreInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    @Autowired
    private IPurchaseApplyDetailInnerServiceSMO purchaseApplyDetailInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;


    @Autowired
    private IResourceStoreV1InnerServiceSMO resourceStoreV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        Assert.hasKeyAndValue(reqJson, "resId", "物品ID不能为空");
        Assert.hasKeyAndValue(reqJson, "storeId", "商户信息不能为空");
        //根据物品id查询采购明细表
        String resId = reqJson.getString("resId");
        PurchaseApplyDetailDto purchaseApplyDetailDto = new PurchaseApplyDetailDto();
        purchaseApplyDetailDto.setResId(resId);
        List<PurchaseApplyDetailDto> purchaseApplyDetailDtos = purchaseApplyDetailInnerServiceSMOImpl.queryPurchaseApplyDetails(purchaseApplyDetailDto);
        Assert.listIsNull(purchaseApplyDetailDtos, "该物品存在采购或领用记录，不能删除！");
        //根据物品id查询调拨记录
        AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
        allocationStorehouseDto.setResId(resId);
        List<AllocationStorehouseDto> allocationStorehouseDtos = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
        Assert.listIsNull(allocationStorehouseDtos, "该物品存在调拨记录，不能删除！");
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException {
        String resId = reqJson.getString("resId");
        reqJson.put("statusCd", "1");
        ResourceStorePo resourceStorePo = BeanConvertUtil.covertBean(reqJson, ResourceStorePo.class);

        int flag = resourceStoreV1InnerServiceSMOImpl.deleteResourceStore(resourceStorePo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        FileRelDto fileRelDto = new FileRelDto();
        fileRelDto.setObjId(resId);
        List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
        if (fileRelDtos != null && fileRelDtos.size() > 0) {
            //删除文件表图片
            for (FileRelDto fileRel : fileRelDtos) {
                FileRelPo fileRelpo = new FileRelPo();
                fileRelpo.setFileRelId(fileRel.getFileRelId());
                flag = fileRelInnerServiceSMOImpl.deleteFileRel(fileRelpo);
                if (flag < 1) {
                    throw new CmdException("保存数据失败");
                }
            }
        }
    }
}
