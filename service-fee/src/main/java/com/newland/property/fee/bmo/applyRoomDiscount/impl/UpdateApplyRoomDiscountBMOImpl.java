package com.newland.property.fee.bmo.applyRoomDiscount.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.fee.bmo.applyRoomDiscount.IUpdateApplyRoomDiscountBMO;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.fee.IApplyRoomDiscountInnerServiceSMO;
import com.newland.property.po.room.ApplyRoomDiscountPo;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("updateApplyRoomDiscountBMOImpl")
public class UpdateApplyRoomDiscountBMOImpl implements IUpdateApplyRoomDiscountBMO {

    @Autowired
    private IApplyRoomDiscountInnerServiceSMO applyRoomDiscountInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;

    /**
     * @param applyRoomDiscountPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> update(ApplyRoomDiscountPo applyRoomDiscountPo) {
        List<String> photos = applyRoomDiscountPo.getPhotos();
        applyRoomDiscountPo.setPhotos(null);
        int flag = applyRoomDiscountInnerServiceSMOImpl.updateApplyRoomDiscount(applyRoomDiscountPo);

        if (flag < 1) {
            return ResultVo.createResponseEntity(ResultVo.CODE_ERROR, "保存失败");
        }
        //获取图片集合
        if (photos != null && photos.size() > 0) {
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(applyRoomDiscountPo.getArdId());
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (fileRelDtos != null && fileRelDtos.size() > 0) {
                FileRelPo fileRelPo = new FileRelPo();
                fileRelPo.setObjId(applyRoomDiscountPo.getArdId());
                fileRelInnerServiceSMOImpl.deleteFileRel(fileRelPo);
            }
            FileRelPo fileRel = new FileRelPo();
            fileRel.setFileRelId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_relId));
            fileRel.setObjId(applyRoomDiscountPo.getArdId());
            //table表示表存储 ftp表示ftp文件存储
            fileRel.setSaveWay("ftp");
            fileRel.setCreateTime(new Date());
            //19000表示装修图片
            fileRel.setRelTypeCd("19000");
            for (String photo : photos) {
                fileRel.setFileRealName(photo);
                fileRel.setFileSaveName(photo);
                fileRelInnerServiceSMOImpl.saveFileRel(fileRel);
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");


    }

}
