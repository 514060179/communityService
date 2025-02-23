package com.newland.property.vo.api.machineTranslate;

import com.newland.property.vo.MorePageVo;

import java.io.Serializable;
import java.util.List;

public class ApiMachineTranslateVo extends MorePageVo implements Serializable {
    List<ApiMachineTranslateDataVo> machineTranslates;


    public List<ApiMachineTranslateDataVo> getMachineTranslates() {
        return machineTranslates;
    }

    public void setMachineTranslates(List<ApiMachineTranslateDataVo> machineTranslates) {
        this.machineTranslates = machineTranslates;
    }
}
