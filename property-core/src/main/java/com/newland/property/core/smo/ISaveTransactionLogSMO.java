package com.newland.property.core.smo;

import com.newland.property.dto.log.AssetImportLogDto;
import com.newland.property.po.log.TransactionLogPo;
import com.newland.property.po.log.TransactionOutLogPo;

/**
 * @ClassName ISaveTransactionLog
 * @Description TODO
 * @Author wuxw
 * @Date 2020/11/16 0:42
 * @Version 1.0
 * add by wuxw 2020/11/16
 **/
public interface ISaveTransactionLogSMO {

    public void saveLog(TransactionLogPo transactionLogPo);


    /**
     * 保存 批量导入功能
     *
     * @param assetImportLogDto
     */
    void saveAssetImportLog(AssetImportLogDto assetImportLogDto);


}
