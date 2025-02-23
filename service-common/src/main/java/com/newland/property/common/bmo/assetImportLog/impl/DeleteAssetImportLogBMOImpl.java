package com.newland.property.common.bmo.assetImportLog.impl;

import com.newland.property.common.bmo.assetImportLog.IDeleteAssetImportLogBMO;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.intf.common.IAssetImportLogInnerServiceSMO;
import com.newland.property.po.log.AssetImportLogPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("deleteAssetImportLogBMOImpl")
public class DeleteAssetImportLogBMOImpl implements IDeleteAssetImportLogBMO {

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    /**
     * @param assetImportLogPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(AssetImportLogPo assetImportLogPo) {

        int flag = assetImportLogInnerServiceSMOImpl.deleteAssetImportLog(assetImportLogPo);

        if (flag > 0) {
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
