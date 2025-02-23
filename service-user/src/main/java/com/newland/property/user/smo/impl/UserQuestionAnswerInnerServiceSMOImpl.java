package com.newland.property.user.smo.impl;


import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.user.UserQuestionAnswerDto;
import com.newland.property.intf.user.IUserQuestionAnswerInnerServiceSMO;
import com.newland.property.po.user.UserQuestionAnswerPo;
import com.newland.property.user.dao.IUserQuestionAnswerServiceDao;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 答卷内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class UserQuestionAnswerInnerServiceSMOImpl extends BaseServiceSMO implements IUserQuestionAnswerInnerServiceSMO {

    @Autowired
    private IUserQuestionAnswerServiceDao userQuestionAnswerServiceDaoImpl;


    @Override
    public int saveUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo) {
        int saveFlag = 1;
        userQuestionAnswerServiceDaoImpl.saveUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerPo));
        return saveFlag;
    }

    @Override
    public int updateUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo) {
        int saveFlag = 1;
        userQuestionAnswerServiceDaoImpl.updateUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerPo));
        return saveFlag;
    }

    @Override
    public int deleteUserQuestionAnswer(@RequestBody UserQuestionAnswerPo userQuestionAnswerPo) {
        int saveFlag = 1;
        userQuestionAnswerPo.setStatusCd("1");
        userQuestionAnswerServiceDaoImpl.updateUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerPo));
        return saveFlag;
    }

    @Override
    public List<UserQuestionAnswerDto> queryUserQuestionAnswers(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto) {

        //校验是否传了 分页信息

        int page = userQuestionAnswerDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            userQuestionAnswerDto.setPage((page - 1) * userQuestionAnswerDto.getRow());
        }

        List<UserQuestionAnswerDto> userQuestionAnswers = BeanConvertUtil.covertBeanList(userQuestionAnswerServiceDaoImpl.getUserQuestionAnswerInfo(BeanConvertUtil.beanCovertMap(userQuestionAnswerDto)), UserQuestionAnswerDto.class);

        return userQuestionAnswers;
    }


    @Override
    public int queryUserQuestionAnswersCount(@RequestBody UserQuestionAnswerDto userQuestionAnswerDto) {
        return userQuestionAnswerServiceDaoImpl.queryUserQuestionAnswersCount(BeanConvertUtil.beanCovertMap(userQuestionAnswerDto));
    }

    public IUserQuestionAnswerServiceDao getUserQuestionAnswerServiceDaoImpl() {
        return userQuestionAnswerServiceDaoImpl;
    }

    public void setUserQuestionAnswerServiceDaoImpl(IUserQuestionAnswerServiceDao userQuestionAnswerServiceDaoImpl) {
        this.userQuestionAnswerServiceDaoImpl = userQuestionAnswerServiceDaoImpl;
    }
}
