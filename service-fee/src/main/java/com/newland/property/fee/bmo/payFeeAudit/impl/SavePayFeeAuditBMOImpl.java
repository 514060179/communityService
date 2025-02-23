package com.newland.property.fee.bmo.payFeeAudit.impl;

import com.newland.property.core.annotation.PropertyTransactional;
import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.dto.fee.FeeDetailDto;
import com.newland.property.fee.bmo.payFeeAudit.ISavePayFeeAuditBMO;
import com.newland.property.intf.fee.IFeeDetailInnerServiceSMO;
import com.newland.property.intf.fee.IPayFeeAuditInnerServiceSMO;
import com.newland.property.po.payFee.PayFeeAuditPo;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("savePayFeeAuditBMOImpl")
public class SavePayFeeAuditBMOImpl implements ISavePayFeeAuditBMO {

    @Autowired
    private IPayFeeAuditInnerServiceSMO payFeeAuditInnerServiceSMOImpl;

    @Autowired
    private IFeeDetailInnerServiceSMO feeDetailInnerServiceSMOImpl;

    /**
     * 添加小区信息
     *
     * @param payFeeAuditPo
     * @return 订单服务能够接受的报文
     */
    @Override
    @PropertyTransactional
    public ResponseEntity<String> save(PayFeeAuditPo payFeeAuditPo) {

        String feeDetailId = payFeeAuditPo.getFeeDetailId();
        String[] feeDetailIds = feeDetailId.split(",");

        for (String tmpFeeDetailId : feeDetailIds) {

            FeeDetailDto feeDetailDto = new FeeDetailDto();
            feeDetailDto.setDetailId(tmpFeeDetailId);
            feeDetailDto.setCommunityId(payFeeAuditPo.getCommunityId());
            List<FeeDetailDto> feeDetailDtos = feeDetailInnerServiceSMOImpl.queryFeeDetails(feeDetailDto);

            if(feeDetailDtos == null || feeDetailDtos.size()<1){
                continue;
            }
            payFeeAuditPo.setFeeId(feeDetailDtos.get(0).getFeeId());
            payFeeAuditPo.setFeeDetailId(feeDetailDtos.get(0).getDetailId());
            payFeeAuditPo.setAuditId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_auditId));
            int flag = payFeeAuditInnerServiceSMOImpl.savePayFeeAudit(payFeeAuditPo);

            if (flag < 1) {
                throw new CmdException("审核保存失败");
            }
        }
        return ResultVo.createResponseEntity(ResultVo.CODE_OK, "保存成功");
    }

}
