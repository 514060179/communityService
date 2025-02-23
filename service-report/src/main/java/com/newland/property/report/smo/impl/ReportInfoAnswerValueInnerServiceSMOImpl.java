package com.newland.property.report.smo.impl;


import com.newland.property.intf.report.IReportInfoAnswerValueInnerServiceSMO;
import com.newland.property.po.reportInfo.ReportInfoAnswerValuePo;
import com.newland.property.report.dao.IReportInfoAnswerValueServiceDao;
import com.newland.property.dto.reportInfo.ReportInfoAnswerValueDto;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 批量操作日志详情内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class ReportInfoAnswerValueInnerServiceSMOImpl extends BaseServiceSMO implements IReportInfoAnswerValueInnerServiceSMO {

    @Autowired
    private IReportInfoAnswerValueServiceDao reportInfoAnswerValueServiceDaoImpl;


    @Override
    public int saveReportInfoAnswerValue(@RequestBody ReportInfoAnswerValuePo reportInfoAnswerValuePo) {
        int saveFlag = 1;
        reportInfoAnswerValueServiceDaoImpl.saveReportInfoAnswerValueInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerValuePo));
        return saveFlag;
    }

     @Override
    public int updateReportInfoAnswerValue(@RequestBody  ReportInfoAnswerValuePo reportInfoAnswerValuePo) {
        int saveFlag = 1;
         reportInfoAnswerValueServiceDaoImpl.updateReportInfoAnswerValueInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerValuePo));
        return saveFlag;
    }

     @Override
    public int deleteReportInfoAnswerValue(@RequestBody  ReportInfoAnswerValuePo reportInfoAnswerValuePo) {
        int saveFlag = 1;
        reportInfoAnswerValuePo.setStatusCd("1");
        reportInfoAnswerValueServiceDaoImpl.updateReportInfoAnswerValueInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerValuePo));
        return saveFlag;
    }

    @Override
    public List<ReportInfoAnswerValueDto> queryReportInfoAnswerValues(@RequestBody  ReportInfoAnswerValueDto reportInfoAnswerValueDto) {

        //校验是否传了 分页信息

        int page = reportInfoAnswerValueDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            reportInfoAnswerValueDto.setPage((page - 1) * reportInfoAnswerValueDto.getRow());
        }

        List<ReportInfoAnswerValueDto> reportInfoAnswerValues = BeanConvertUtil.covertBeanList(reportInfoAnswerValueServiceDaoImpl.getReportInfoAnswerValueInfo(BeanConvertUtil.beanCovertMap(reportInfoAnswerValueDto)), ReportInfoAnswerValueDto.class);

        return reportInfoAnswerValues;
    }


    @Override
    public int queryReportInfoAnswerValuesCount(@RequestBody ReportInfoAnswerValueDto reportInfoAnswerValueDto) {
        return reportInfoAnswerValueServiceDaoImpl.queryReportInfoAnswerValuesCount(BeanConvertUtil.beanCovertMap(reportInfoAnswerValueDto));    }

    public IReportInfoAnswerValueServiceDao getReportInfoAnswerValueServiceDaoImpl() {
        return reportInfoAnswerValueServiceDaoImpl;
    }

    public void setReportInfoAnswerValueServiceDaoImpl(IReportInfoAnswerValueServiceDao reportInfoAnswerValueServiceDaoImpl) {
        this.reportInfoAnswerValueServiceDaoImpl = reportInfoAnswerValueServiceDaoImpl;
    }
}
