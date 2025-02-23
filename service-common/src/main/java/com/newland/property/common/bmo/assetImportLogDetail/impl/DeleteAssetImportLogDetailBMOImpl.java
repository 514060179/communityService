package com.newland.property.common.bmo.assetImportLogDetail.impl;

import com.newland.property.common.bmo.assetImportLogDetail.IDeleteAssetImportLogDetailBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.newland.property.po.log.AssetImportLogDetailPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAssetImportLogDetailBMOImpl")
public class DeleteAssetImportLogDetailBMOImpl implements IDeleteAssetImportLogDetailBMO {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    /**
     * @param assetImportLogDetailPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AssetImportLogDetailPo assetImportLogDetailPo) {

        int flag = assetImportLogDetailInnerServiceSMOImpl.deleteAssetImportLogDetail(assetImportLogDetailPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
