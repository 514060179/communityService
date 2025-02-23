package com.newland.property.common.smo.impl;


import com.newland.property.common.dao.IFileRelServiceDao;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import com.newland.property.dto.file.FileRelDto;
import com.newland.property.intf.common.IFileRelInnerServiceSMO;
import com.newland.property.intf.user.IUserInnerServiceSMO;
import com.newland.property.po.file.FileRelPo;
import com.newland.property.utils.util.BeanConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 文件存放内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FileRelInnerServiceSMOImpl extends BaseServiceSMO implements IFileRelInnerServiceSMO {

    @Autowired
    private IFileRelServiceDao fileRelServiceDaoImpl;

    @Autowired
    private IUserInnerServiceSMO userInnerServiceSMOImpl;

    @Override
    public List<FileRelDto> queryFileRels(@RequestBody FileRelDto fileRelDto) {

        //校验是否传了 分页信息

        int page = fileRelDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            fileRelDto.setPage((page - 1) * fileRelDto.getRow());
        }

        List<FileRelDto> fileRels = BeanConvertUtil.covertBeanList(fileRelServiceDaoImpl.getFileRelInfo(BeanConvertUtil.beanCovertMap(fileRelDto)), FileRelDto.class);


        return fileRels;
    }

    /**
     * 保存文件关系
     *
     * @param fileRelPo
     * @return
     */
    @Override
    public int saveFileRel(@RequestBody FileRelPo fileRelPo) {
        return fileRelServiceDaoImpl.saveFileRel(BeanConvertUtil.beanCovertMap(fileRelPo));
    }

    @Override
    public int updateFileRel(@RequestBody FileRelPo fileRelPo) {
        fileRelServiceDaoImpl.updateFileRelInfoInstance(BeanConvertUtil.beanCovertMap(fileRelPo));
        return 1;
    }

    @Override
    public int deleteFileRel(@RequestBody FileRelPo fileRelPo) {
        return fileRelServiceDaoImpl.deleteFileRel(BeanConvertUtil.beanCovertMap(fileRelPo));
    }


    @Override
    public int queryFileRelsCount(@RequestBody FileRelDto fileRelDto) {
        return fileRelServiceDaoImpl.queryFileRelsCount(BeanConvertUtil.beanCovertMap(fileRelDto));
    }

    public IFileRelServiceDao getFileRelServiceDaoImpl() {
        return fileRelServiceDaoImpl;
    }

    public void setFileRelServiceDaoImpl(IFileRelServiceDao fileRelServiceDaoImpl) {
        this.fileRelServiceDaoImpl = fileRelServiceDaoImpl;
    }

    public IUserInnerServiceSMO getUserInnerServiceSMOImpl() {
        return userInnerServiceSMOImpl;
    }

    public void setUserInnerServiceSMOImpl(IUserInnerServiceSMO userInnerServiceSMOImpl) {
        this.userInnerServiceSMOImpl = userInnerServiceSMOImpl;
    }
}
