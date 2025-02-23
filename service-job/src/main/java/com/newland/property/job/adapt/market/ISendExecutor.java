package com.newland.property.job.adapt.market;


import com.newland.property.dto.market.MarketTextDto;

public interface ISendExecutor {

    /**
     * 发送信息
     * @param marketTextDto
     */
    void send(MarketTextDto marketTextDto,String tel,String communityId,String communityName);
}
