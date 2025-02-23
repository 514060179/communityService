package com.newland.property.common.bmo.sysDocument.impl;

import com.newland.property.common.bmo.sysDocument.IGetSysDocumentBMO;
import com.newland.property.dto.sysDocument.SysDocumentDto;
import com.newland.property.intf.common.ISysDocumentInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getSysDocumentBMOImpl")
public class GetSysDocumentBMOImpl implements IGetSysDocumentBMO {

    @Autowired
    private ISysDocumentInnerServiceSMO sysDocumentInnerServiceSMOImpl;

    /**
     * @param sysDocumentDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(SysDocumentDto sysDocumentDto) {


        int count = sysDocumentInnerServiceSMOImpl.querySysDocumentsCount(sysDocumentDto);

        List<SysDocumentDto> sysDocumentDtos = null;
        if (count > 0) {
            sysDocumentDtos = sysDocumentInnerServiceSMOImpl.querySysDocuments(sysDocumentDto);
        } else {
            sysDocumentDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) sysDocumentDto.getRow()), count, sysDocumentDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
