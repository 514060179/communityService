package com.newland.property.store.bmo.storeAds.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileDto;
import com.newland.property.intf.common.IFileInnerServiceSMO;
import com.newland.property.intf.store.IStoreAdsInnerServiceSMO;
import com.newland.property.po.store.StoreAdsPo;
import com.newland.property.store.bmo.storeAds.IUpdateStoreAdsBMO;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service("updateStoreAdsBMOImpl")
public class UpdateStoreAdsBMOImpl implements IUpdateStoreAdsBMO {

    @Autowired
    private IStoreAdsInnerServiceSMO storeAdsInnerServiceSMOImpl;

    @Autowired
    private IFileInnerServiceSMO fileInnerServiceSMOImpl;

    /**
     * @param storeAdsPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(StoreAdsPo storeAdsPo) {

        if (StringUtil.isEmpty(storeAdsPo.getUrl())) {
            throw new IllegalArgumentException("未包含图片信息");
        }

        //保存图片
        FileDto fileDto = new FileDto();
        fileDto.setFileId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_file_id));
        fileDto.setFileName(fileDto.getFileId());
        fileDto.setContext(storeAdsPo.getUrl());
        fileDto.setSuffix("jpeg");
        fileDto.setCommunityId(storeAdsPo.getShareId());
        String fileName = fileInnerServiceSMOImpl.saveFile(fileDto);
        storeAdsPo.setUrl(fileName);

        int flag = storeAdsInnerServiceSMOImpl.updateStoreAds(storeAdsPo);

        if (flag > 0) {
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
