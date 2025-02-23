package com.newland.property.store.bmo.contractFile.impl;

import com.newland.property.dto.contract.ContractFileDto;
import com.newland.property.intf.store.IContractFileInnerServiceSMO;
import com.newland.property.store.bmo.contractFile.IGetContractFileBMO;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.constant.MappingConstant;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getContractFileBMOImpl")
public class GetContractFileBMOImpl implements IGetContractFileBMO {

    @Autowired
    private IContractFileInnerServiceSMO contractFileInnerServiceSMOImpl;

    /**
     *
     *
     * @param  contractFileDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ContractFileDto contractFileDto) {


        int count = contractFileInnerServiceSMOImpl.queryContractFilesCount(contractFileDto);

        List<ContractFileDto> contractFileDtos = null;
        if (count > 0) {
            contractFileDtos = contractFileInnerServiceSMOImpl.queryContractFiles(contractFileDto);
            freshUrl(contractFileDtos);
        } else {
            contractFileDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) contractFileDto.getRow()), count, contractFileDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshUrl(List<ContractFileDto> contractFileDtos) {
        String imgPath = MappingCache.getValue(MappingConstant.FILE_DOMAIN,"IMG_PATH");
        for(ContractFileDto contractFileDto : contractFileDtos){
            contractFileDto.setFileSaveName(imgPath+contractFileDto.getFileSaveName());
        }
    }

}
