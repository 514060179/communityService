/*
 * Copyright 2017-2020 吴学文 and newland property team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.newland.property.common.bmo.machine.impl;

import com.newland.property.common.bmo.machine.IUpdateMachineTransactionStateBMO;
import com.newland.property.dto.machine.MachineTranslateDto;
import com.newland.property.intf.common.IMachineTranslateInnerServiceSMO;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 保存 开门记录
 *
 * @desc add by 吴学文 17:37
 */
@Service
public class UpdateMachineTransactionStateBMOImpl implements IUpdateMachineTransactionStateBMO {


    @Autowired
    private IMachineTranslateInnerServiceSMO machineTranslateInnerServiceSMOImpl;

    @Override
    public ResponseEntity<String> update(MachineTranslateDto machineTranslateDto) {
        int count = machineTranslateInnerServiceSMOImpl.updateMachineTranslateState(machineTranslateDto);
        if (count > 0) {
            return ResultVo.success();
        }
        return ResultVo.error("上传记录失败");
    }
}
