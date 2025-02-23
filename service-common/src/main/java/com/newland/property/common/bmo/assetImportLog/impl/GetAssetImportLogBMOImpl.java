package com.newland.property.common.bmo.assetImportLog.impl;

import com.newland.property.common.bmo.assetImportLog.IGetAssetImportLogBMO;
import com.newland.property.dto.log.AssetImportLogDto;
import com.newland.property.intf.common.IAssetImportLogInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getAssetImportLogBMOImpl")
public class GetAssetImportLogBMOImpl implements IGetAssetImportLogBMO {

    @Autowired
    private IAssetImportLogInnerServiceSMO assetImportLogInnerServiceSMOImpl;

    /**
     * @param assetImportLogDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(AssetImportLogDto assetImportLogDto) {


        int count = assetImportLogInnerServiceSMOImpl.queryAssetImportLogsCount(assetImportLogDto);

        List<AssetImportLogDto> assetImportLogDtos = null;
        if (count > 0) {
            assetImportLogDtos = assetImportLogInnerServiceSMOImpl.queryAssetImportLogs(assetImportLogDto);
        } else {
            assetImportLogDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) assetImportLogDto.getRow()), count, assetImportLogDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
