package com.newland.property.fee.bmo.feeManualCollectionDetail.impl;

import com.newland.property.dto.fee.FeeManualCollectionDetailDto;
import com.newland.property.fee.bmo.feeManualCollectionDetail.IGetFeeManualCollectionDetailBMO;
import com.newland.property.intf.fee.IFeeManualCollectionDetailInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeManualCollectionDetailBMOImpl")
public class GetFeeManualCollectionDetailBMOImpl implements IGetFeeManualCollectionDetailBMO {

    @Autowired
    private IFeeManualCollectionDetailInnerServiceSMO feeManualCollectionDetailInnerServiceSMOImpl;

    /**
     * @param feeManualCollectionDetailDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(FeeManualCollectionDetailDto feeManualCollectionDetailDto) {


        int count = feeManualCollectionDetailInnerServiceSMOImpl.queryFeeManualCollectionDetailsCount(feeManualCollectionDetailDto);

        List<FeeManualCollectionDetailDto> feeManualCollectionDetailDtos = null;
        if (count > 0) {
            feeManualCollectionDetailDtos = feeManualCollectionDetailInnerServiceSMOImpl.queryFeeManualCollectionDetails(feeManualCollectionDetailDto);
        } else {
            feeManualCollectionDetailDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeManualCollectionDetailDto.getRow()), count, feeManualCollectionDetailDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
