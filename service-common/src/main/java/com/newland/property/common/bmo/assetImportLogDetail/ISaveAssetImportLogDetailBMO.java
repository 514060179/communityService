package com.newland.property.common.bmo.assetImportLogDetail;

import com.newland.property.po.log.AssetImportLogDetailPo;
import org.springframework.http.ResponseEntity;
public interface ISaveAssetImportLogDetailBMO {


    /**
     * 添加批量操作日志详情
     * add by wuxw
     * @param assetImportLogDetailPo
     * @return
     */
    ResponseEntity<String> save(AssetImportLogDetailPo assetImportLogDetailPo);


}
