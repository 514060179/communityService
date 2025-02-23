package com.newland.property.fee.bmo.applyRoomDiscount.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.fee.bmo.applyRoomDiscount.IDeleteApplyRoomDiscountBMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountPo;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("deleteApplyRoomDiscountBMOImpl")
public class DeleteApplyRoomDiscountBMOImpl implements IDeleteApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountPo 数据
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> delete(ApplyRoomDiscountPo applyRoomDiscountPo) {

        int flag = applyRoomDiscountInnerServiceSMOImpl.deleteApplyRoomDiscount(applyRoomDiscountPo);

        if (flag > 0) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(applyRoomDiscountPo.getArdId());
            //查询文件表
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setObjId(applyRoomDiscountPo.getArdId());
                fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
            }
            return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
        }

        return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
    }

}
