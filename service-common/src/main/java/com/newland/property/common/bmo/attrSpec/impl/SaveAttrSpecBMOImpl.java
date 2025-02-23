package com.newland.property.common.bmo.attrSpec.impl;

import com.newland.property.common.bmo.attrSpec.ISaveAttrSpecBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IAttrSpecInnerServiceSMO;
import com.newland.property.po.attrSpec.AttrSpecPo;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAttrSpecBMOImpl")
public class SaveAttrSpecBMOImpl implements ISaveAttrSpecBMO {

    @Autowired
    private IAttrSpecInnerServiceSMO attrSpecInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param attrSpecPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AttrSpecPo attrSpecPo) {
        attrSpecPo.setSpecId(GenerateCodeFactory.getGeneratorId("11"));
        if(StringUtil.isEmpty(attrSpecPo.getSpecCd())) {
            attrSpecPo.setSpecCd(GenerateCodeFactory.getSpecCd());
        }
        int flag = attrSpecInnerServiceSMOImpl.saveAttrSpec(attrSpecPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
