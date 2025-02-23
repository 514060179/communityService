package com.newland.property.intf.fee;


import com.newland.property.dto.fee.TempCarFeeResult;
import com.newland.property.dto.machine.CarInoutDto;
import com.newland.property.dto.fee.TempCarFeeConfigAttrDto;
import com.newland.property.dto.fee.TempCarFeeConfigDto;

import java.util.List;

/**
 * 计算 临时车 停车费
 */
public interface IComputeTempCarFee {


    /**
     * 临时车停车费计算
     *
     * @param carInoutDto
     * @param tempCarFeeConfigDto
     * @return
     */
    TempCarFeeResult computeTempCarFee(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto) throws Exception;


     TempCarFeeResult computeTempCarFee(CarInoutDto carInoutDto, TempCarFeeConfigDto tempCarFeeConfigDto, List<TempCarFeeConfigAttrDto> tempCarFeeConfigAttrDtos) throws Exception;
}
