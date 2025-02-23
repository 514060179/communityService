package com.newland.property.report.smo.impl;


import com.newland.property.intf.report.IReportInfoSettingInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoSettingPo;
import com.newland.property.report.dao.IReportInfoSettingServiceDao;
import com.newland.property.dto.reportInfo.ReportInfoSettingDto;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 进出上报内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportInfoSettingInnerServiceSMOImpl extends BaseServiceSMO implements IReportInfoSettingInnerServiceSMO {

    @Autowired
    private IReportInfoSettingServiceDao reportInfoSettingServiceDaoImpl;


    @Override
    public int saveReportInfoSetting(@RequestBody ReportInfoSettingPo reportInfoSettingPo) {
        int saveFlag = 1;
        reportInfoSettingServiceDaoImpl.saveReportInfoSettingInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingPo));
        return saveFlag;
    }

     @Override
    public int updateReportInfoSetting(@RequestBody  ReportInfoSettingPo reportInfoSettingPo) {
        int saveFlag = 1;
         reportInfoSettingServiceDaoImpl.updateReportInfoSettingInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingPo));
        return saveFlag;
    }

     @Override
    public int deleteReportInfoSetting(@RequestBody  ReportInfoSettingPo reportInfoSettingPo) {
        int saveFlag = 1;
        reportInfoSettingPo.setStatusCd("1");
        reportInfoSettingServiceDaoImpl.updateReportInfoSettingInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingPo));
        return saveFlag;
    }

    @Override
    public List<ReportInfoSettingDto> queryReportInfoSettings(@RequestBody  ReportInfoSettingDto reportInfoSettingDto) {

        //校验是否传了 分页信息

        int page = reportInfoSettingDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoSettingDto.setPage((page - 1) * reportInfoSettingDto.getRow());
        }

        List<ReportInfoSettingDto> reportInfoSettings = BeanConvertUtil.covertBeanList(reportInfoSettingServiceDaoImpl.getReportInfoSettingInfo(BeanConvertUtil.beanCovertMap(reportInfoSettingDto)), ReportInfoSettingDto.class);

        return reportInfoSettings;
    }


    @Override
    public int queryReportInfoSettingsCount(@RequestBody ReportInfoSettingDto reportInfoSettingDto) {
        return reportInfoSettingServiceDaoImpl.queryReportInfoSettingsCount(BeanConvertUtil.beanCovertMap(reportInfoSettingDto));    }

    public IReportInfoSettingServiceDao getReportInfoSettingServiceDaoImpl() {
        return reportInfoSettingServiceDaoImpl;
    }

    public void setReportInfoSettingServiceDaoImpl(IReportInfoSettingServiceDao reportInfoSettingServiceDaoImpl) {
        this.reportInfoSettingServiceDaoImpl = reportInfoSettingServiceDaoImpl;
    }
}
