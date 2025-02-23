package com.newland.property.job.export.adapt;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.data.ExportDataDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.privilege.BasePrivilegeDto;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.community.IMenuInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.intf.user.IOwnerStatisticsServiceBMO;
import com.newland.property.job.export.IExportDataAdapt;
import com.newland.property.utils.util.Assert;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.utils.util.StringUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 业主信息导出
 */
@Service("listOwnerExport")
public class ListOwnerExportAdapt implements IExportDataAdapt {

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IOwnerStatisticsServiceBMO ownerStatisticsServiceBMOImpl;

    @Autowired
    private IMenuInnerServiceSMO menuInnerServiceSMOImpl;

    @Autowired
    private IFileRelInnerServiceSMO fileRelInnerServiceSMOImpl;


    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;
    private static final int MAX_ROW = 60000;


    @Override
    public SXSSFWorkbook exportData(ExportDataDto exportDataDto) {
        //工作簿
        SXSSFWorkbook workbook = null;
        //工作表
        workbook = new SXSSFWorkbook();
        workbook.setCompressTempFiles(false);
        Sheet sheet = workbook.createSheet("业主信息表");
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("业主ID");
        row.createCell(1).setCellValue("业主人脸");
        row.createCell(2).setCellValue("姓名");
        row.createCell(3).setCellValue("性别");
        row.createCell(4).setCellValue("身份证");
        row.createCell(5).setCellValue("联系方式");
        row.createCell(6).setCellValue("家庭住址");
        row.createCell(7).setCellValue("房屋数");
        row.createCell(8).setCellValue("业主成员");
        row.createCell(9).setCellValue("车辆数");
        row.createCell(10).setCellValue("投诉");
        row.createCell(11).setCellValue("报修");
        row.createCell(12).setCellValue("欠费");
        row.createCell(13).setCellValue("业主合同");
        row.createCell(14).setCellValue("门禁钥匙");
        JSONObject reqJson = exportDataDto.getReqJson();
        //查询数据
        getOwner(sheet, reqJson);

        return workbook;

    }

    private void getOwner(Sheet sheet, JSONObject reqJson) {
        //根据房屋查询时 先用 房屋信息查询 业主ID
        OwnerDto ownerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        freshRoomId(reqJson);
        //根据成员查询
        queryByOwnerMember(reqJson, ownerDto);
        //查询总记录数
        int count = ownerInnerServiceSMOImpl.queryOwnersCount(ownerDto);
        double record = Math.ceil((double) count / MAX_ROW);
        if (count < 1) {
            return;
        }
        List<OwnerDto> ownerDtoList = null;
        for (int page = 1; page <= record; page++) {
            ownerDto.setPage(page);
            ownerDto.setRow(MAX_ROW);
            ownerDtoList = handleData(reqJson, ownerDto);
            appendData(ownerDtoList, sheet, (page - 1) * MAX_ROW);
        }
    }

    private List<OwnerDto> handleData(JSONObject reqJson, OwnerDto ownerDto) {
        List<OwnerDto> ownerDtos = new ArrayList<>();
        if (reqJson.containsKey("name") && !StringUtil.isEmpty(reqJson.getString("name"))) {
            return queryByCondition(reqJson);
        }
        List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwners(ownerDto);
        // 查询统计数据
        ownerDtoList = ownerStatisticsServiceBMOImpl.query(ownerDtoList);
        List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", reqJson.getString("userId"));
        for (OwnerDto tmpOwnerDto : ownerDtoList) {
            //查询照片
            FileRelDto fileRelDto = new FileRelDto();
            fileRelDto.setObjId(tmpOwnerDto.getMemberId());
            fileRelDto.setRelTypeCd("10000"); //业主照片
            List<FileRelDto> fileRelDtos = fileRelInnerServiceSMOImpl.queryFileRels(fileRelDto);
            if (!ListUtil.isNull(fileRelDtos)) {
                List<String> urls = new ArrayList<>();
                for (FileRelDto fileRel : fileRelDtos) {
                    urls.add(fileRel.getFileRealName());
                }
                tmpOwnerDto.setUrls(urls);
            }
            //对业主身份证号隐藏处理
            String idCard = tmpOwnerDto.getIdCard();
            if (mark.size() == 0 && idCard != null && !"".equals(idCard) && idCard.length() > 16) {
                idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                tmpOwnerDto.setIdCard(idCard);
            }
            //对业主手机号隐藏处理
            String link = tmpOwnerDto.getLink();
            if (mark.size() == 0 && link != null && !"".equals(link) && link.length() == 11) {
                link = link.substring(0, 3) + "****" + link.substring(7);
                tmpOwnerDto.setLink(link);
            }
            ownerDtos.add(tmpOwnerDto);
        }
        return ownerDtos;
    }


    private List<OwnerDto> queryByCondition(JSONObject reqJson) {
        //获取当前用户id
        String ownerTypeCd = reqJson.getString("ownerTypeCd");
        OwnerDto tmpOwnerDto = BeanConvertUtil.covertBean(reqJson, OwnerDto.class);
        if (!StringUtil.isEmpty(ownerTypeCd) && ownerTypeCd.contains(",")) {
            tmpOwnerDto.setOwnerTypeCd("");
            tmpOwnerDto.setOwnerTypeCds(ownerTypeCd.split(","));
        }
        String userId = reqJson.getString("userId");
        int total = ownerInnerServiceSMOImpl.queryOwnerCountByCondition(tmpOwnerDto);
        List<OwnerDto> ownerDtos = new ArrayList<>();
        if (total > 0) {
            List<OwnerDto> ownerDtoList = ownerInnerServiceSMOImpl.queryOwnersByCondition(tmpOwnerDto);
            // 查询统计数据
            ownerDtoList = ownerStatisticsServiceBMOImpl.query(ownerDtoList);

            List<Map> mark = getPrivilegeOwnerList("/roomCreateFee", userId);
            for (OwnerDto ownerDto : ownerDtoList) {
                //对业主身份证号隐藏处理
                String idCard = ownerDto.getIdCard();
                if (mark.size() == 0 && !StringUtil.isEmpty(idCard) && idCard.length() > 16) {
                    idCard = idCard.substring(0, 6) + "**********" + idCard.substring(16);
                    ownerDto.setIdCard(idCard);
                }
                //对业主手机号隐藏处理
                String link = ownerDto.getLink();
                if (mark.size() == 0 && !StringUtil.isEmpty(link) && link.length() == 11) {
                    link = link.substring(0, 3) + "****" + link.substring(7);
                    ownerDto.setLink(link);
                }
                ownerDtos.add(ownerDto);
            }
        }
        return ownerDtos;
    }

    /**
     * 脱敏处理
     *
     * @return
     */
    public List<Map> getPrivilegeOwnerList(String resource, String userId) {
        BasePrivilegeDto basePrivilegeDto = new BasePrivilegeDto();
        basePrivilegeDto.setResource(resource);
        basePrivilegeDto.setUserId(userId);
        List<Map> privileges = menuInnerServiceSMOImpl.checkUserHasResource(basePrivilegeDto);
        return privileges;
    }

    private void freshRoomId(JSONObject reqJson) {
        if (!reqJson.containsKey("roomName")) {
            return;
        }
        String roomName = reqJson.getString("roomName");
        if (StringUtil.isEmpty(roomName)) {
            return;
        }
        if (!roomName.contains("-")) {
            throw new IllegalArgumentException("房屋格式错误,请写入如 楼栋-单元-房屋 格式");
        }
        String[] params = roomName.split("-", 3);
        if (params.length != 3) {
            throw new IllegalArgumentException("房屋格式错误,请写入如 楼栋-单元-房屋 格式");
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(params[0]);
        roomDto.setUnitNum(params[1]);
        roomDto.setRoomNum(params[2]);
        roomDto.setCommunityId(reqJson.getString("communityId"));
        List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        Assert.listOnlyOne(roomDtos, "未查询到房屋下业主信息");
        reqJson.put("roomId", roomDtos.get(0).getRoomId());
    }

    /**
     * 根据 成员查询
     *
     * @param reqJson
     * @param ownerDto
     */
    private void queryByOwnerMember(JSONObject reqJson, OwnerDto ownerDto) {

        if (!reqJson.containsKey("memberName") && !reqJson.containsKey("memberLink")) {
            return;
        }

        String memberName = reqJson.getString("memberName");
        String memberLink = reqJson.getString("memberLink");

        if (StringUtil.isEmpty(memberName) && StringUtil.isEmpty(memberLink)) {
            return;
        }

        OwnerDto tmpOwnerMemberDto = new OwnerDto();
        tmpOwnerMemberDto.setNameLike(memberName);
        tmpOwnerMemberDto.setLink(memberLink);
        tmpOwnerMemberDto.setOwnerTypeCds(new String[]{OwnerDto.OWNER_TYPE_CD_MEMBER,
                OwnerDto.OWNER_TYPE_CD_OTHER,
                OwnerDto.OWNER_TYPE_CD_TEMP,
                OwnerDto.OWNER_TYPE_CD_RENTING
        });
        List<OwnerDto> ownerMembers = ownerInnerServiceSMOImpl.queryOwnerMembers(tmpOwnerMemberDto);

        if (ListUtil.isNull(ownerMembers)) {
            ownerDto.setOwnerId("-1"); // 写入-1 查询不到数据
            return;
        }

        List<String> ownerIds = new ArrayList<>();
        for (OwnerDto tmpOwnerMember : ownerMembers) {
            ownerIds.add(tmpOwnerMember.getOwnerId());
        }

        ownerDto.setOwnerIds(ownerIds.toArray(new String[ownerIds.size()]));
    }

    private void appendData(List<OwnerDto> ownerDtoList, Sheet sheet, int step) {

        Row row = null;
        JSONObject dataObj = null;
        for (int roomIndex = 0; roomIndex < ownerDtoList.size(); roomIndex++) {
            row = sheet.createRow(roomIndex + step + 1);
            dataObj = JSONObject.parseObject(JSONObject.toJSONString(ownerDtoList.get(roomIndex)));
            row.createCell(0).setCellValue(handleCellValue(dataObj.getString("memberId")));
            row.createCell(1).setCellValue(handleCellValue(dataObj.getString("url")));
            row.createCell(2).setCellValue(handleCellValue(dataObj.getString("name")));
            row.createCell(3).setCellValue(handleCellValue(handleSex(dataObj.getString("sex"))));
            row.createCell(4).setCellValue(handleCellValue(dataObj.getString("idCard")));
            row.createCell(5).setCellValue(handleCellValue(dataObj.getString("link")));
            row.createCell(6).setCellValue(handleCellValue(dataObj.getString("address")));
            row.createCell(7).setCellValue(handleNum(dataObj.getString("roomCount")));
            row.createCell(8).setCellValue(handleNum(dataObj.getString("memberCount")));
            row.createCell(9).setCellValue(handleNum(dataObj.getString("carCount")));
            row.createCell(10).setCellValue(handleNum(dataObj.getString("complaintCount")));
            row.createCell(11).setCellValue(handleNum(dataObj.getString("repairCount")));
            row.createCell(12).setCellValue(handleNum(dataObj.getString("oweFee")));
            row.createCell(13).setCellValue(handleNum(dataObj.getString("contractCount")));
            row.createCell(14).setCellValue(handleNum(dataObj.getString("listValues")));
        }
    }

    private String handleCellValue(String value) {
        return (value == null || "".equals(value.trim())) ? "-" : value;
    }

    private String handleSex(String sex) {
        return "".equals("0") ? "男" : "女";
    }

    private String handleNum(String value) {
        return (value == null || "".equals(value.trim())) ? "0" : value;
    }

}
