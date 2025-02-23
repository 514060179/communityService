package com.newland.property.fee.bmo.fee;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.contract.ContractDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.fee.bmo.IApiBaseBMO;
import com.newland.property.po.fee.FeeAttrPo;

/**
 * @ClassName IFeeBMO
 * @Description TODO
 * @Author wuxw
 * @Date 2020/3/9 22:22
 * @Version 1.0
 * add by wuxw 2020/3/9
 **/
public interface IFeeBMO extends IApiBaseBMO {



    /**
     * 添加费用明细信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param
     * @return 订单服务能够接受的报文
     */
    JSONObject addFeePreDetail(JSONObject paramInJson);

    /**
     * 修改费用信息
     *
     * @param paramInJson     接口调用放传入入参
     * @param
     * @return 订单服务能够接受的报文
     */
    JSONObject modifyPreFee(JSONObject paramInJson);



    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param context 数据上下文
     * @return 订单服务能够接受的报文
     */
    JSONObject addFee(OwnerCarDto ownerCarDto, JSONObject paramInJson, ICmdDataFlowContext context);


    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    FeeAttrPo addFeeAttr(JSONObject paramInJson, ICmdDataFlowContext dataFlowContext, String specCd, String value);



    /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
     JSONObject addRoomFee(RoomDto roomDto, JSONObject paramInJson, ICmdDataFlowContext dataFlowContext) ;   /**
     * 添加物业费用
     *
     * @param paramInJson     接口调用放传入入参
     * @param dataFlowContext 数据上下文
     * @return 订单服务能够接受的报文
     */
    JSONObject addContractFee(ContractDto contractDto, JSONObject paramInJson, ICmdDataFlowContext dataFlowContext);

}
