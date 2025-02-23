package com.newland.property.common.bmo.assetImportLogDetail.impl;

import com.newland.property.common.bmo.assetImportLogDetail.ISaveAssetImportLogDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.newland.property.po.log.AssetImportLogDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("saveAssetImportLogDetailBMOImpl")
public class SaveAssetImportLogDetailBMOImpl implements ISaveAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param assetImportLogDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(AssetImportLogDetailPo assetImportLogDetailPo) {

        assetImportLogDetailPo.setDetailId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_detailId));
        int flag = assetImportLogDetailInnerServiceSMOImpl.saveAssetImportLogDetail(assetImportLogDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
