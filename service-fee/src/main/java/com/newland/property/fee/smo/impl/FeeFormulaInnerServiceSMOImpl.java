package com.newland.property.fee.smo.impl;


import com.newland.property.fee.dao.IFeeFormulaServiceDao;
import com.newland.property.intf.fee.IFeeFormulaInnerServiceSMO;
import com.newland.property.dto.fee.FeeFormulaDto;
import com.newland.property.po.fee.FeeFormulaPo;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.core.base.smo.BaseServiceSMO;
import com.newland.property.dto.PageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName FloorInnerServiceSMOImpl
 * @Description 费用公式内部服务实现类
 * @Author wuxw
 * @Date 2019/4/24 9:20
 * @Version 1.0
 * add by wuxw 2019/4/24
 **/
@RestController
public class FeeFormulaInnerServiceSMOImpl extends BaseServiceSMO implements IFeeFormulaInnerServiceSMO {

    @Autowired
    private IFeeFormulaServiceDao feeFormulaServiceDaoImpl;


    @Override
    public int saveFeeFormula(@RequestBody FeeFormulaPo feeFormulaPo) {
        int saveFlag = 1;
        feeFormulaServiceDaoImpl.saveFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaPo));
        return saveFlag;
    }

     @Override
    public int updateFeeFormula(@RequestBody  FeeFormulaPo feeFormulaPo) {
        int saveFlag = 1;
         feeFormulaServiceDaoImpl.updateFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaPo));
        return saveFlag;
    }

     @Override
    public int deleteFeeFormula(@RequestBody  FeeFormulaPo feeFormulaPo) {
        int saveFlag = 1;
        feeFormulaPo.setStatusCd("1");
        feeFormulaServiceDaoImpl.updateFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaPo));
        return saveFlag;
    }

    @Override
    public List<FeeFormulaDto> queryFeeFormulas(@RequestBody  FeeFormulaDto feeFormulaDto) {

        //校验是否传了 分页信息

        int page = feeFormulaDto.getPage();

        if (page != PageDto.DEFAULT_PAGE) {
            feeFormulaDto.setPage((page - 1) * feeFormulaDto.getRow());
        }

        List<FeeFormulaDto> feeFormulas = BeanConvertUtil.covertBeanList(feeFormulaServiceDaoImpl.getFeeFormulaInfo(BeanConvertUtil.beanCovertMap(feeFormulaDto)), FeeFormulaDto.class);

        return feeFormulas;
    }


    @Override
    public int queryFeeFormulasCount(@RequestBody FeeFormulaDto feeFormulaDto) {
        return feeFormulaServiceDaoImpl.queryFeeFormulasCount(BeanConvertUtil.beanCovertMap(feeFormulaDto));    }

    public IFeeFormulaServiceDao getFeeFormulaServiceDaoImpl() {
        return feeFormulaServiceDaoImpl;
    }

    public void setFeeFormulaServiceDaoImpl(IFeeFormulaServiceDao feeFormulaServiceDaoImpl) {
        this.feeFormulaServiceDaoImpl = feeFormulaServiceDaoImpl;
    }
}
