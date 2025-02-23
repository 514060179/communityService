package com.newland.property.job.adapt.hcIotNew;

import com.newland.property.dto.owner.OwnerDto;

public interface IOwnerDataToIot {

    /**
     * 同步业主数据
     * @param ownerDto
     */
    void sendOwnerData(OwnerDto ownerDto);
}
