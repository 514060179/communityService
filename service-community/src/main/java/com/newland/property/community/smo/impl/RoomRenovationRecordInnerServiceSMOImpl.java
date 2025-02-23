package com.newland.property.community.smo.impl;

import com.newland.property.community.dao.IRoomRenovationRecordServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.intf.community.IRoomRenovationRecordInnerServiceSMO;
import com.newland.property.po.room.RoomRenovationRecordPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomRenovationRecordInnerServiceSMOImpl extends BaseServiceSMO implements IRoomRenovationRecordInnerServiceSMO {

    @Autowired
    private IRoomRenovationRecordServiceDao roomRenovationRecordServiceDaoImpl;

    @Override
    public int saveRoomRenovationRecord(RoomRenovationRecordPo roomRenovationRecordPo) {
        int saveFlag = 1;
        roomRenovationRecordServiceDaoImpl.saveRoomRenovationRecordInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
        return saveFlag;
    }

    @Override
    public List<RoomRenovationRecordPo> queryRoomRenovationRecords(RoomRenovationRecordPo roomRenovationRecordPo) {
        //校验是否传了 分页信息
        int page = roomRenovationRecordPo.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationRecordPo.setPage((page - 1) * roomRenovationRecordPo.getRow());
        }
        List<RoomRenovationRecordPo> roomRenovationRecordPos = BeanConvertUtil.covertBeanList(roomRenovationRecordServiceDaoImpl.getRoomRenovationRecordsInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo)), RoomRenovationRecordPo.class);
        return roomRenovationRecordPos;
    }

    @Override
    public List<RoomRenovationRecordPo> getRoomRenovationRecords(RoomRenovationRecordPo roomRenovationRecordPo) {
        //校验是否传了 分页信息
        int page = roomRenovationRecordPo.getPage();
        if (page != PageDto.DEFAULT_PAGE) {
            roomRenovationRecordPo.setPage((page - 1) * roomRenovationRecordPo.getRow());
        }
        List<RoomRenovationRecordPo> roomRenovationRecordPos = BeanConvertUtil.covertBeanList(roomRenovationRecordServiceDaoImpl.findRoomRenovationRecordsInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo)), RoomRenovationRecordPo.class);
        return roomRenovationRecordPos;
    }

    @Override
    public int queryRoomRenovationRecordsCount(RoomRenovationRecordPo roomRenovationRecordPo) {
        return roomRenovationRecordServiceDaoImpl.queryRoomRenovationRecordsCount(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
    }

    @Override
    public int getRoomRenovationRecordsCount(RoomRenovationRecordPo roomRenovationRecordPo) {
        return roomRenovationRecordServiceDaoImpl.getRoomRenovationRecordsCount(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
    }

    @Override
    public int deleteRoomRenovationRecord(RoomRenovationRecordPo roomRenovationRecordPo) {
        int saveFlag = 1;
        roomRenovationRecordPo.setStatusCd("1");
        roomRenovationRecordServiceDaoImpl.updateRoomRenovationRecordInfo(BeanConvertUtil.beanCovertMap(roomRenovationRecordPo));
        return saveFlag;
    }

}
