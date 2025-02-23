package com.newland.property.community.bmo.assets.impl;

import com.alibaba.fastjson.JSONArray;
import com.newland.property.community.bmo.assets.IQueryAssetsOpenDoorBMO;
import com.newland.property.intf.common.IMachineRecordInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QueryAssetsOpenDoorBMOImpl implements IQueryAssetsOpenDoorBMO {
    protected final static Logger logger = LoggerFactory.getLogger(QueryAssetsOpenDoorBMOImpl.class);

    @Autowired
    private IMachineRecordInnerServiceSMO machineRecordInnerServiceSMOImpl;


    /**
     * @param communityId
     * @return {
     * data:{
     * floorCount:30,
     * roomCount:29,
     * parkingSpaceCount:12,
     * machineCount:12
     * }
     * }
     */
    @Override
    public ResponseEntity<String> query(String communityId) {
        JSONArray data = machineRecordInnerServiceSMOImpl.getAssetsMachineRecords(communityId);

        return ResultVo.createResponseEntity(data);
    }

    @Override
    public ResponseEntity<String> queryImage(String communityId) {
        return null;
    }


}
