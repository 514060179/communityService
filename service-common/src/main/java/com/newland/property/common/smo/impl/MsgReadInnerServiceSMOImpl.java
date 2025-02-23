package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.IMsgReadServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.intf.common.IMsgReadInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.msg.MsgReadDto;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 消息阅读内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class MsgReadInnerServiceSMOImpl extends BaseServiceSMO implements IMsgReadInnerServiceSMO {

    @Autowired
    private IMsgReadServiceDao msgReadServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<MsgReadDto> queryMsgReads(@RequestBody MsgReadDto msgReadDto) {

        //校验是否传了 分页信息

        int page = msgReadDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            msgReadDto.setPage((page - 1) * msgReadDto.getRow());
        }

        List<MsgReadDto> msgReads = BeanConvertUtil.covertBeanList(msgReadServiceDaoImpl.getMsgReadInfo(BeanConvertUtil.beanCovertMap(msgReadDto)), MsgReadDto.class);

        return msgReads;
    }


    @Override
    public int queryMsgReadsCount(@RequestBody MsgReadDto msgReadDto) {
        return msgReadServiceDaoImpl.queryMsgReadsCount(BeanConvertUtil.beanCovertMap(msgReadDto));
    }

    public IMsgReadServiceDao getMsgReadServiceDaoImpl() {
        return msgReadServiceDaoImpl;
    }

    public void setMsgReadServiceDaoImpl(IMsgReadServiceDao msgReadServiceDaoImpl) {
        this.msgReadServiceDaoImpl = msgReadServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
