package com.newland.property.user.bmo.owner;

import com.newland.property.dto.owner.OwnerDto;

import java.util.List;

/**
 * 查询业主 房屋 车辆等统计信息
 */
public interface IQueryOwnerStatisticsBMO {

    /**
     * 查询业主统计信息
     * @param ownerDtos
     * @return
     */
    List<OwnerDto> query(List<OwnerDto> ownerDtos);
}
