package com.newland.property.common.bmo.assetImportLogDetail;
import com.newland.property.po.log.AssetImportLogDetailPo;
import org.springframework.http.ResponseEntity;

public interface IDeleteAssetImportLogDetailBMO {


    /**
     * 修改批量操作日志详情
     * add by wuxw
     * @param assetImportLogDetailPo
     * @return
     */
    ResponseEntity<String> delete(AssetImportLogDetailPo assetImportLogDetailPo);


}
