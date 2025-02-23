package com.newland.property.fee.bmo.applyRoomDiscount.impl;

import com.newland.property.dto.room.ApplyRoomDiscountDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.fee.bmo.applyRoomDiscount.IGetApplyRoomDiscountBMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.newland.property.utils.util.StringUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getApplyRoomDiscountBMOImpl")
public class GetApplyRoomDiscountBMOImpl implements IGetApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(ApplyRoomDiscountDto applyRoomDiscountDto) {


        int count = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscountsCount(applyRoomDiscountDto);

        List<ApplyRoomDiscountDto> applyRoomDiscountDtos = new ArrayList<>();
        if (count > 0) {
            List<ApplyRoomDiscountDto> applyRoomDiscounts = applyRoomDiscountInnerServiceSMOImpl.queryApplyRoomDiscounts(applyRoomDiscountDto);
//            String imgUrl = MappingCache.getValue("IMG_PATH");
            for (ApplyRoomDiscountDto applyRoomDiscount : applyRoomDiscounts) {
                FileRelDto fileRelDto = new FileRelDto();
                fileRelDto.setObjId(applyRoomDiscount.getArdId());
                List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
                List<String> urls = new ArrayList<>();
                for (FileRelDto fileRel : fileRelDtos) {
                    if (!StringUtil.isEmpty(fileRel.getFileRealName()) && "19000".equals(fileRel.getRelTypeCd())) {
                        urls.add(fileRel.getFileRealName());
                    } else if (!StringUtil.isEmpty(fileRel.getFileRealName()) && "21000".equals(fileRel.getRelTypeCd())) {
                        urls.add(fileRel.getFileRealName());
                    }
                }
                applyRoomDiscount.setUrls(urls);
                applyRoomDiscountDtos.add(applyRoomDiscount);
            }
        } else {
            applyRoomDiscountDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) applyRoomDiscountDto.getRow()), count, applyRoomDiscountDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

}
