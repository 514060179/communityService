package com.newland.property.common.bmo.assetImportLog;
import com.newland.property.po.log.AssetImportLogPo;
import org.springframework.http.ResponseEntity;

public interface IUpdateAssetImportLogBMO {


    /**
     * 修改批量操作日志
     * add by wuxw
     * @param assetImportLogPo
     * @return
     */
    ResponseEntity<String> update(AssetImportLogPo assetImportLogPo);


}
