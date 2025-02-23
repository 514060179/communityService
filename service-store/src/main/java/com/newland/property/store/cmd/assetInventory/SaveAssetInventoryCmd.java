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
package com.newland.property.store.cmd.assetInventory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IAssetInventoryDetailV1InnerServiceSMO;
import com.newland.property.intf.store.IAssetInventoryV1InnerServiceSMO;
import com.newland.property.intf.store.IAllocationStorehouseApplyInnerServiceSMO;
import com.newland.property.intf.store.IAllocationStorehouseInnerServiceSMO;
import com.newland.property.intf.store.IPurchaseApplyInnerServiceSMO;
import com.newland.property.po.assetInventory.AssetInventoryPo;
import com.newland.property.po.assetInventory.AssetInventoryDetailPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类表述：保存
 * 服务编码：assetInventory.saveAssetInventory
 * 请求路劲：/app/assetInventory.SaveAssetInventory
 * add by 吴学文 at 2022-09-22 14:23:28 mail: 928255095@qq.com
 * open source address: https://gitee.com/wuxw7/MicroCommunity
 * 官网：http://www.homecommunity.cn
 * 温馨提示：如果您对此文件进行修改 请不要删除原有作者及注释信息，请补充您的 修改的原因以及联系邮箱如下
 * // modify by 张三 at 2021-09-12 第10行在某种场景下存在某种bug 需要修复，注释10至20行 加入 20行至30行
 */
@NewlandPropertyCmd(serviceCode = "assetInventory.saveAssetInventory")
public class SaveAssetInventoryCmd extends Cmd {

    private static Logger logger = LoggerFactory.getLogger(SaveAssetInventoryCmd.class);

    public static final String CODE_PREFIX_ID = "10";

    @Autowired
    private IPurchaseApplyInnerServiceSMO purchaseApplyInnerServiceSMOImpl;

    @Autowired
    private IAssetInventoryV1InnerServiceSMO assetInventoryV1InnerServiceSMOImpl;

    @Autowired
    private IAssetInventoryDetailV1InnerServiceSMO assetInventoryDetailV1InnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseInnerServiceSMO allocationStorehouseInnerServiceSMOImpl;

    @Autowired
    private IAllocationStorehouseApplyInnerServiceSMO allocationStorehouseApplyInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {
        Assert.hasKeyAndValue(reqJson, "invTime", "请求报文中未包含盘点时间");
        Assert.hasKeyAndValue(reqJson, "communityId", "请求报文中未包含communityId");
        Assert.hasKeyAndValue(reqJson, "shId", "请求报文中未包含shId");
        reqJson.put("staffId", reqJson.getString("userId"));
    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {
        AssetInventoryPo assetInventoryPo = BeanConvertUtil.covertBean(reqJson, AssetInventoryPo.class);

//        PurchaseApplyDto purchaseApplyDto = new PurchaseApplyDto();
//        purchaseApplyDto.setCommunityId(assetInventoryPo.getCommunityId());
//        purchaseApplyDto.setState(purchaseApplyDto.STATE_DEALING);//审核中
//        List<PurchaseApplyDto> purchaseApplyDtos = purchaseApplyInnerServiceSMOImpl.queryPurchaseApplyAndDetails(purchaseApplyDto);
//        for (PurchaseApplyDto applyDto : purchaseApplyDtos){
//            List<PurchaseApplyDetailVo> purchaseApplyDetailVos = applyDto.getPurchaseApplyDetailVo();
//            for (PurchaseApplyDetailVo applyDetailVo : purchaseApplyDetailVos) {
//                 if (applyDetailVo.getShId() != null && assetInventoryPo.getShId() != null && applyDetailVo.getShId().equals(assetInventoryPo.getShId())) {
//                    String resOrderType =  applyDto.getResOrderType().equals("10000")? "入库":"出库";
//                    throw new CmdException("盘存失败"+applyDetailVo.getShName() + "有"+ resOrderType +"物品");
//                }
//            }
//        }
//        AllocationStorehouseDto allocationStorehouseDto = new AllocationStorehouseDto();
//        allocationStorehouseDto.setShIda(assetInventoryPo.getShId());
//        allocationStorehouseDto.setState(allocationStorehouseDto.STATE_AUDIT);
//        List<AllocationStorehouseDto> allocationStorehouseDtosa = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
//        if (allocationStorehouseDtosa.size() > 0){
//            //cmdDataFlowContext.setResponseEntity(ResultVo.error("盘存失败"+assetInventoryPo.getShName() + "有调拨物品。" ));
//            throw new CmdException("盘存失败"+assetInventoryPo.getShName() + "有调拨物品。");
//        }
//        allocationStorehouseDto.setShIdz(assetInventoryPo.getShId());
//        allocationStorehouseDtosa = allocationStorehouseInnerServiceSMOImpl.queryAllocationStorehouses(allocationStorehouseDto);
//        if (allocationStorehouseDtosa.size() > 0){
//            //cmdDataFlowContext.setResponseEntity(ResultVo.error("盘存失败"+assetInventoryPo.getShName() + "有调拨物品。" ));
//            throw new CmdException("盘存失败"+assetInventoryPo.getShName() + "有调拨物品。");
//        }

        //保存盘点 产品
        if (!reqJson.containsKey("resourceStores")) {
            throw new IllegalArgumentException("未包含盘点物品");
        }
        JSONArray resourceStores = reqJson.getJSONArray("resourceStores");
        if (resourceStores == null || resourceStores.size() < 1) {
            throw new IllegalArgumentException("未包含盘点物品");
        }

        assetInventoryPo.setAiId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
        for (int resourceStoreIndex = 0; resourceStoreIndex < resourceStores.size(); resourceStoreIndex++) {
            JSONObject resourceStore = resourceStores.getJSONObject(resourceStoreIndex);
            resourceStore.put("originalStock", resourceStore.getString("stock"));
            AssetInventoryDetailPo assetInventoryDetailPo = BeanConvertUtil.covertBean(resourceStore, AssetInventoryDetailPo.class);
            assetInventoryDetailPo.setId(GenerateCodeFactory.getGeneratorId(CODE_PREFIX_ID));
            assetInventoryDetailPo.setApplyOrderId(assetInventoryPo.getAiId());
            int flag = assetInventoryDetailV1InnerServiceSMOImpl.saveAssetInventoryDetail(assetInventoryDetailPo);
            if (flag < 1) {
                throw new CmdException("保存数据盘点明细失败");
            }
        }
        //assetInventoryPo.setState("4488"); //4488 盘点中，4477 盘点结束，4466 盘点入库
        int flag = assetInventoryV1InnerServiceSMOImpl.saveAssetInventory(assetInventoryPo);
        if (flag < 1) {
            throw new CmdException("保存数据失败");
        }
        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
