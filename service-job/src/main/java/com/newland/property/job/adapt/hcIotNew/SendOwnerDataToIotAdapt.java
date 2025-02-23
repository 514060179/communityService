package com.newland.property.job.adapt.hcIotNew;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.system.Business;
import com.newland.property.intf.user.IOwnerV1InnerServiceSMO;
import com.newland.property.job.adapt.DatabusAdaptImpl;
import com.newland.property.utils.cache.MappingCache;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 同步业主信息
 */
@Component(value = "sendOwnerDataToIotAdapt")
public class SendOwnerDataToIotAdapt extends DatabusAdaptImpl {

    @Autowired
    private IOwnerV1InnerServiceSMO ownerV1InnerServiceSMOImpl;

    @Autowired
    private IOwnerDataToIot ownerDataToIotImpl;

    @Override
    public void execute(Business business, List<Business> businesses) {
        String iotSwitch = MappingCache.getValue("IOT", "IOT_SWITCH");
        if (!"ON".equals(iotSwitch)) {
            return;
        }

        JSONObject data = business.getData();
        String memberId = data.getString("memberId");
        if (StringUtil.isEmpty(memberId)) {
            throw new IllegalArgumentException("未包含业主信息");
        }

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setMemberId(memberId);
        List<OwnerDto> ownerDtos = ownerV1InnerServiceSMOImpl.queryOwners(ownerDto);

        if (ListUtil.isNull(ownerDtos)) {
            throw new IllegalArgumentException("业主不存在");
        }
        ownerDataToIotImpl.sendOwnerData(ownerDtos.get(0));
    }
}
