package com.newland.property.fee.bmo.feeDiscountRule.impl;

import com.newland.property.dto.fee.FeeDiscountRuleDto;
import com.newland.property.dto.fee.FeeDiscountRuleSpecDto;
import com.newland.property.fee.bmo.feeDiscountRule.IGetFeeDiscountRuleBMO;
import com.newland.property.intf.fee.IFeeDiscountRuleInnerServiceSMO;
import com.newland.property.intf.fee.IFeeDiscountRuleSpecInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("getFeeDiscountRuleBMOImpl")
public class GetFeeDiscountRuleBMOImpl implements IGetFeeDiscountRuleBMO {

    @Autowired
    private IFeeDiscountRuleInnerServiceSMO feeDiscountRuleInnerServiceSMOImpl;

    @Autowired
    private IFeeDiscountRuleSpecInnerServiceSMO feeDiscountRuleSpecInnerServiceSMOImpl;

    /**
     * @param feeDiscountRuleDto
     * @return 订单服务能够接受的报文
     */
    @Override
    public ResponseEntity<String> get(FeeDiscountRuleDto feeDiscountRuleDto) {


        int count = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRulesCount(feeDiscountRuleDto);

        List<FeeDiscountRuleDto> feeDiscountRuleDtos = null;
        if (count > 0) {
            feeDiscountRuleDtos = feeDiscountRuleInnerServiceSMOImpl.queryFeeDiscountRules(feeDiscountRuleDto);

            freshRuleSpec(feeDiscountRuleDtos);
        } else {
            feeDiscountRuleDtos = new ArrayList<>();
        }

        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) feeDiscountRuleDto.getRow()), count, feeDiscountRuleDtos);

        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);

        return responseEntity;
    }

    private void freshRuleSpec(List<FeeDiscountRuleDto> feeDiscountRuleDtos) {

        List<String> ruleIds = new ArrayList<>();
        for (FeeDiscountRuleDto feeDiscountRuleDto : feeDiscountRuleDtos) {
            ruleIds.add(feeDiscountRuleDto.getRuleId());
        }

        if (ruleIds.size() < 1) {
            return;
        }


        FeeDiscountRuleSpecDto feeDiscountRuleSpecDto = new FeeDiscountRuleSpecDto();
        feeDiscountRuleSpecDto.setRuleIds(ruleIds.toArray(new String[ruleIds.size()]));
        List<FeeDiscountRuleSpecDto> feeDiscountRuleSpecDtos
                = feeDiscountRuleSpecInnerServiceSMOImpl.queryFeeDiscountRuleSpecs(feeDiscountRuleSpecDto);
        List<FeeDiscountRuleSpecDto> tmpSpecs = null;
        for (FeeDiscountRuleDto feeDiscountRuleDto : feeDiscountRuleDtos) {
            tmpSpecs = new ArrayList<>();
            for (FeeDiscountRuleSpecDto tmpFeeDiscountRuleSpecDto : feeDiscountRuleSpecDtos) {
                if (feeDiscountRuleDto.getRuleId().equals(tmpFeeDiscountRuleSpecDto.getRuleId())) {
                    tmpSpecs.add(tmpFeeDiscountRuleSpecDto);
                }
            }
            feeDiscountRuleDto.setFeeDiscountRuleSpecs(tmpSpecs);
        }
    }

}
