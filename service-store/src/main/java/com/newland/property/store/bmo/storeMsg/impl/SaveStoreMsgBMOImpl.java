package com.newland.property.store.bmo.storeMsg.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IStoreMsgInnerServiceSMO;
import com.newland.property.po.store.StoreMsgPo;
import com.newland.property.store.bmo.storeMsg.ISaveStoreMsgBMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveStoreMsgBMOImpl")
public class SaveStoreMsgBMOImpl implements ISaveStoreMsgBMO {

    @Autowired
    private IStoreMsgInnerServiceSMO storeMsgInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param storeMsgPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(StoreMsgPo storeMsgPo) {

        storeMsgPo.setMsgId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_msgId));
        int flag = storeMsgInnerServiceSMOImpl.saveStoreMsg(storeMsgPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
