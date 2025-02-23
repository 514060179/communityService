package com.newland.property.fee.bmo.feeCollectionDetail.impl;

import com.newland.property.dto.fee.FeeCollectionDetailDto;
import com.newland.property.fee.bmo.feeCollectionDetail.IGetFeeCollectionDetailBMO;
import com.newland.property.intf.fee.IFeeCollectionDetailInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeCollectionDetailBMOImpl")
public class GetFeeCollectionDetailBMOImpl implements IGetFeeCollectionDetailBMO {

    @Autowired
    private IFeeCollectionDetailInnerServiceSMO feeCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeCollectionDetailDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(FeeCollectionDetailDto feeCollectionDetailDto) {


        int count = feeCollectionDetailInnerServiceSMOImpl.queryFeeCollectionDetailsCount(feeCollectionDetailDto);

        List<FeeCollectionDetailDto> feeCollectionDetailDtos = null;
        if (count > 0) {
            feeCollectionDetailDtos = feeCollectionDetailInnerServiceSMOImpl.queryFeeCollectionDetails(feeCollectionDetailDto);
        } else {
            feeCollectionDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeCollectionDetailDto.getRow()), count, feeCollectionDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
