package com.newland.property.order.smo.impl;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.system.IdSeqDto;
import com.newland.property.intf.order.IIdSeqInnerServiceSMO;
import com.newland.property.order.dao.ISnowflakeldWorker;
import com.newland.property.service.init.ServiceInfoListener;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 用户服务信息管理业务信息实现
 * Created by wuxw on 2017/4/5.
 */
@RestController(value = "com.newland.property.intf.order.IIdSeqInnerServiceSMO")
public class IdSeqInnerServiceSMOImpl extends BaseServiceSMO implements IIdSeqInnerServiceSMO {
    protected final static Logger logger = LoggerFactory.getLogger(IdSeqInnerServiceSMOImpl.class);


    @Autowired
    ISnowflakeldWorker snowflakeIdWorkerImpl;

    @Autowired
    private ServiceInfoListener serviceInfoListener;

    /**
     * 根据sequence 表中name 查询ID
     *
     * @param primaryKeyInfo name信息封装
     * @return
     */
    @Override
    public JSONObject queryPrimaryKey(@RequestBody  JSONObject primaryKeyInfo) throws Exception {
        Map paramIn = JSONObject.toJavaObject(primaryKeyInfo, Map.class);
//        Map primaryKey = iPrimaryKeyServiceDao.queryPrimaryKey(paramIn);
//        JSONObject returnPrimaryKey = new JSONObject();
//        if (primaryKey != null && primaryKey.containsKey("targetId")) {
//            returnPrimaryKey.put("targetId", primaryKey.get("targetId"));
//        } else {
//            //如果没定义相应name的键序列，直接返回-1 表示 自己系统需要自己生成
//            returnPrimaryKey.put("targetId", "-1");
//        }
        return null;
    }

    @Override
    public IdSeqDto generateCode(@RequestBody  IdSeqDto idSeqDto) {
        String code = snowflakeIdWorkerImpl.getIdByPrefix(idSeqDto.getPrefix(), serviceInfoListener.getWorkId());

        idSeqDto.setId(code);

        return idSeqDto;
    }

    public ServiceInfoListener getServiceInfoListener() {
        return serviceInfoListener;
    }

    public void setServiceInfoListener(ServiceInfoListener serviceInfoListener) {
        this.serviceInfoListener = serviceInfoListener;
    }


    public ISnowflakeldWorker getSnowflakeIdWorkerImpl() {
        return snowflakeIdWorkerImpl;
    }

    public void setSnowflakeIdWorkerImpl(ISnowflakeldWorker snowflakeIdWorkerImpl) {
        this.snowflakeIdWorkerImpl = snowflakeIdWorkerImpl;
    }
}
