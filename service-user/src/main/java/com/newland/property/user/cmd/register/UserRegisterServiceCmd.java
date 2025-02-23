package com.newland.property.user.cmd.register;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.intf.store.IStoreInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.intf.user.IUserV1InnerServiceSMO;
import com.newland.property.po.user.UserPo;
import com.newland.property.utils.constant.UserLevelConstant;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.vo.ResultVo;
import org.slf4j.Logger;
import com.newland.property.core.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 服务注册功能迁移
 */
@NewlandPropertyCmd(serviceCode = "user.service.register")
public class UserRegisterServiceCmd extends Cmd {
    private final static Logger logger = LoggerFactory.getLogger(UserRegisterServiceCmd.class);
    @Autowired
    private IUserV1InnerServiceSMO userV1InnerServiceSMOImpl;

    @Autowired
    private IStoreInnerServiceSMO storeInnerServiceSMOImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) {

    }

    @Override
    @PropertyTransactional
    public void doCmd(CmdEvent event, ICmdDataFlowContext cmdDataFlowContext, JSONObject reqJson) throws CmdException {

        reqJson.put("userId", GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_userId));
        reqJson.put("levelCd", UserLevelConstant.USER_LEVEL_ADMIN);

        UserPo userPo = BeanConvertUtil.covertBean(reqJson, UserPo.class);
        userPo.setbId("-1");
        int flag = userV1InnerServiceSMOImpl.saveUser(userPo);
        if (flag < 1) {
            throw new CmdException("注册失败");
        }

        cmdDataFlowContext.setResponseEntity(ResultVo.success());
    }
}
