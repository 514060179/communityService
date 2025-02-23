package com.newland.property.report.bmo.search.impl;

import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.data.SearchDataDto;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.community.IRoomV1InnerServiceSMO;
import com.newland.property.report.bmo.search.ISearchRoomBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchRoomBMOImpl implements ISearchRoomBMO {

    @Autowired
    private IRoomV1InnerServiceSMO roomV1InnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<RoomDto> roomDtos = new ArrayList<>();

        // todo 根据房屋编号 查询房屋信息
        queryRoomByRoomNum(searchDataDto, roomDtos);

        // todo 根据 楼栋单元房屋 去查询房屋信息
        queryRoomByRoomName(searchDataDto, roomDtos);

        searchDataDto.setRooms(roomDtos);

        return searchDataDto;
    }

    private void queryRoomByRoomName(SearchDataDto searchDataDto, List<RoomDto> roomDtos) {

        String searchValue = searchDataDto.getSearchValue();
        //1栋1单元1001室
        if(searchValue.contains("栋")){
            searchValue = searchValue.replace("栋","-").replace("单元","-").replace("室","");
        }

        if (!searchValue.contains("-")) {
            return;
        }
        String[] values = searchValue.split("-", 3);

        if (values.length != 3) {
            return;
        }

        RoomDto roomDto = new RoomDto();
        roomDto.setFloorNum(values[0]);
        roomDto.setUnitNum(values[1]);
        roomDto.setRoomNum(values[2]);
        roomDto.setCommunityId(searchDataDto.getCommunityId());

        List<RoomDto> tmpRoomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
        if (tmpRoomDtos == null || tmpRoomDtos.size() < 1) {
            return;
        }

        roomDtos.addAll(tmpRoomDtos);
    }

    /**
     * 根据房屋编号查询
     *
     * @param searchDataDto
     * @param roomDtos
     */
    private void queryRoomByRoomNum(SearchDataDto searchDataDto, List<RoomDto> roomDtos) {
        RoomDto roomDto = new RoomDto();
        roomDto.setRoomNumLike(searchDataDto.getSearchValue());
        roomDto.setCommunityId(searchDataDto.getCommunityId());
        List<RoomDto> tmpRoomDtos = roomV1InnerServiceSMOImpl.queryRooms(roomDto);
        if (tmpRoomDtos == null || tmpRoomDtos.size() < 1) {
            return;
        }
        for(RoomDto tmpRoomDto : tmpRoomDtos){
            tmpRoomDto.setRoomName(tmpRoomDto.getFloorNum()+"-"+tmpRoomDto.getUnitNum()+"-"+tmpRoomDto.getRoomNum());
        }
        roomDtos.addAll(tmpRoomDtos);
    }
}
