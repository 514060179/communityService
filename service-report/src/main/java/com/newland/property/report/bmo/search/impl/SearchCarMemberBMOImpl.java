package com.newland.property.report.bmo.search.impl;

import com.newland.property.dto.data.SearchDataDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.intf.user.IOwnerCarV1InnerServiceSMO;
import com.newland.property.report.bmo.search.ISearchCarMemberBMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchCarMemberBMOImpl implements ISearchCarMemberBMO {
    @Autowired
    private IOwnerCarV1InnerServiceSMO ownerCarV1InnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {
        List<OwnerCarDto> ownerCarMemberDtos = new ArrayList<>();

        queryCarMembersByCarNum(searchDataDto, ownerCarMemberDtos);

        searchDataDto.setCarMembers(ownerCarMemberDtos);

        return searchDataDto;
    }

    private void queryCarMembersByCarNum(SearchDataDto searchDataDto, List<OwnerCarDto> ownerCarMemberDtos) {
        OwnerCarDto ownerCarDto = new OwnerCarDto();
        ownerCarDto.setCarNumLike(searchDataDto.getSearchValue());
        ownerCarDto.setCommunityId(searchDataDto.getCommunityId());
        ownerCarDto.setCarTypeCd(OwnerCarDto.CAR_TYPE_MEMBER);
        List<OwnerCarDto> tmpOwnerCarDtos = ownerCarV1InnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

        if (tmpOwnerCarDtos == null || tmpOwnerCarDtos.size() < 1) {
            return;
        }

        ownerCarMemberDtos.addAll(tmpOwnerCarDtos);
    }
}
