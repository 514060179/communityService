package com.newland.property.common.bmo.assetImportLogDetail.impl;

import com.newland.property.common.bmo.assetImportLogDetail.IUpdateAssetImportLogDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.newland.property.po.log.AssetImportLogDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateAssetImportLogDetailBMOImpl")
public class UpdateAssetImportLogDetailBMOImpl implements IUpdateAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * @param assetImportLogDetailPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(AssetImportLogDetailPo assetImportLogDetailPo) {

        int flag = assetImportLogDetailInnerServiceSMOImpl.updateAssetImportLogDetail(assetImportLogDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
