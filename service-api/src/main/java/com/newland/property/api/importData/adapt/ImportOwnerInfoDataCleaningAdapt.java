package com.newland.property.api.importData.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.api.importData.DefaultImportDataAdapt;
import com.newland.property.api.importData.IImportDataCleaningAdapt;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.importData.ImportOwnerInfoDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.system.ComponentValidateResult;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.ImportExcelUtils;
import com.newland.property.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Moonny
 */
@Service("importOwnerInfoDataCleaning")
public class ImportOwnerInfoDataCleaningAdapt extends DefaultImportDataAdapt implements IImportDataCleaningAdapt {

    @Override
    public List analysisExcel(Workbook workbook, JSONObject paramIn, ComponentValidateResult result) throws Exception {
        List<ImportOwnerInfoDto> ownerInfoDtos = new ArrayList<>();
        //封装对象
        this.getOwnerInfos(workbook, ownerInfoDtos, result);
        //数据格式校验
        validateOwnerInfo(ownerInfoDtos);

        return ownerInfoDtos;
    }

    /**
     * 获取业主信息
     *
     * @param workbook
     * @param ownerInfoDtos
     */
    private void getOwnerInfos(Workbook workbook, List<ImportOwnerInfoDto> ownerInfoDtos,
                            ComponentValidateResult result) {
        Sheet sheet;
        sheet = ImportExcelUtils.getSheet(workbook, "业主信息");
        List<Object[]> oList = ImportExcelUtils.listFromSheet(sheet);
        ImportOwnerInfoDto importOwnerInfoDto;
        for (int osIndex = 0; osIndex < oList.size(); osIndex++) {

            Object[] os = oList.get(osIndex);

            if (osIndex == 0) { // 第一行是 头部信息 直接跳过
                continue;
            }
            if (os == null || StringUtil.isNullOrNone(os[0])) {
                continue;
            }

            Assert.hasValue(os[0], (osIndex + 1) + "行姓名不能为空");
            Assert.hasValue(os[1], (osIndex + 1) + "行性别不能为空");
            Assert.hasValue(os[2], (osIndex + 1) + "行手机区号不能为空");
            Assert.hasValue(os[3], (osIndex + 1) + "行手机号不能为空");
            // Assert.hasValue(os[4], (osIndex + 1) + "行身份证号不能为空");
            Assert.hasValue(os[5], (osIndex + 1) + "行业主类型不能为空");
//            Assert.hasValue(os[6], (osIndex + 1) + "行家庭住址不能为空");
//            Assert.hasValue(os[7], (osIndex + 1) + "行备注不能为空");
//            Assert.hasValue(os[8], (osIndex + 1) + "行业主手机区号不能为空");
//            Assert.hasValue(os[9], (osIndex + 1) + "行业主手机号不能为空");

            importOwnerInfoDto = new ImportOwnerInfoDto();
            importOwnerInfoDto.setCommunityId(result.getCommunityId());
            importOwnerInfoDto.setUserId(result.getUserId());
            importOwnerInfoDto.setName(os[0].toString().trim());
            importOwnerInfoDto.setSex(os[1].toString().trim());
            importOwnerInfoDto.setAreaCode(os[2].toString().trim());
            importOwnerInfoDto.setLink(os[3].toString().trim());
            importOwnerInfoDto.setIdCard(os[4].toString().trim());
            importOwnerInfoDto.setOwnerTypeCd(os[5].toString().trim());
            importOwnerInfoDto.setAddress(os[6].toString().trim());
            importOwnerInfoDto.setRemark(os[7].toString().trim());

//            importOwnerInfoDto.setBuiltUpArea(os[8].toString().trim());
//            importOwnerInfoDto.setRoomArea(os[9].toString().trim());
//            importOwnerInfoDto.setRoomRent(os[10].toString().trim());
//            importOwnerInfoDto.setRoomState(os[11].toString().trim());

            String memberId = GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_ownerId);
            importOwnerInfoDto.setMemberId(memberId);
            if (OwnerDto.OWNER_TYPE_CD_OWNER.equals(importOwnerInfoDto.getOwnerTypeCd())) {
                importOwnerInfoDto.setOwnerId(memberId);
            }

            ownerInfoDtos.add(importOwnerInfoDto);
        }
    }

    /**
     * 数据格式校验
     *
     * @param ownerInfoDtos
     */
    private void validateOwnerInfo(List<ImportOwnerInfoDto> ownerInfoDtos) {
        ImportOwnerInfoDto importOwnerInfoDto;
        ImportOwnerInfoDto tmpImportOwnerInfoDto;
        boolean hasOwnerType;
        for (int roomIndex = 0; roomIndex < ownerInfoDtos.size(); roomIndex++) {
            importOwnerInfoDto = ownerInfoDtos.get(roomIndex);

            if (StringUtil.isEmpty(importOwnerInfoDto.getName())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行姓名不能为空");
            }

            if (StringUtil.isEmpty(importOwnerInfoDto.getSex())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行性别不能为空");
            }

            if (StringUtil.isEmpty(importOwnerInfoDto.getAreaCode())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行手机区号不能为空");
            }

            if (StringUtil.isEmpty(importOwnerInfoDto.getLink())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行手机号码不能为空");
            }

            if (StringUtil.isEmpty(importOwnerInfoDto.getOwnerTypeCd())) {
                throw new IllegalArgumentException((roomIndex + 2) + "行业主类型不能为空");
            }

            // 校验成员之前是否存在
            hasOwnerType = false;
            for (int preRoomIndex = 0; preRoomIndex < roomIndex; preRoomIndex++) {
                tmpImportOwnerInfoDto = ownerInfoDtos.get(preRoomIndex);
                if (tmpImportOwnerInfoDto.getAreaCode().equals(importOwnerInfoDto.getAreaCode())
                        && tmpImportOwnerInfoDto.getLink().equals(importOwnerInfoDto.getLink())
                        && OwnerDto.OWNER_TYPE_CD_OWNER.equals(tmpImportOwnerInfoDto.getOwnerTypeCd())) {
                    hasOwnerType = true;
                    break;
                }
            }

            if (!hasOwnerType) {
                throw new IllegalArgumentException((roomIndex + 2) + "行的手机号重复");
            }
        }
    }
}
