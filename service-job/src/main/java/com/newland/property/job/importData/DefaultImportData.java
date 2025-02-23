package com.newland.property.job.importData;

import com.newland.property.dto.log.AssetImportLogDetailDto;
import com.newland.property.intf.common.IAssetImportLogDetailInnerServiceSMO;
import com.newland.property.po.log.AssetImportLogDetailPo;
import com.newland.property.utils.util.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultImportData {

    @Autowired
    private IAssetImportLogDetailInnerServiceSMO assetImportLogDetailInnerServiceSMOImpl;

    protected void updateImportLogDetailState(String detailId) {
        String state = AssetImportLogDetailDto.STATE_SUCCESS_IMPORT;
        String message = "导入成功";
        AssetImportLogDetailPo tmpAssetImportOwnerRoomPo = new AssetImportLogDetailPo();
        tmpAssetImportOwnerRoomPo.setDetailId(detailId);
        tmpAssetImportOwnerRoomPo.setState(state);
        tmpAssetImportOwnerRoomPo.setMessage(message);
        assetImportLogDetailInnerServiceSMOImpl.updateAssetImportLogDetail(tmpAssetImportOwnerRoomPo);
    }

    protected void updateImportLogDetailState(String detailId, Exception e) {
        String state = AssetImportLogDetailDto.STATE_FAIL_IMPORT;
        String message = ExceptionUtil.getStackTrace(e);
        AssetImportLogDetailPo tmpAssetImportOwnerRoomPo = new AssetImportLogDetailPo();
        tmpAssetImportOwnerRoomPo.setDetailId(detailId);
        tmpAssetImportOwnerRoomPo.setState(state);
        tmpAssetImportOwnerRoomPo.setMessage(message);
        assetImportLogDetailInnerServiceSMOImpl.updateAssetImportLogDetail(tmpAssetImportOwnerRoomPo);
    }
}
