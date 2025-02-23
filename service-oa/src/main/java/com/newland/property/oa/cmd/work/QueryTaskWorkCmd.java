package com.newland.property.oa.cmd.work;

import com.alibaba.fastjson.JSONObject;
import com.newland.property.core.annotation.NewlandPropertyCmd;
import com.newland.property.core.context.CmdContextUtils;
import com.newland.property.core.context.ICmdDataFlowContext;
import com.newland.property.core.event.cmd.Cmd;
import com.newland.property.core.event.cmd.CmdEvent;
import com.newland.property.dto.workCopy.WorkCopyDto;
import com.newland.property.dto.workPool.WorkPoolDto;
import com.newland.property.dto.workTask.WorkTaskDto;
import com.newland.property.intf.oa.*;
import com.newland.property.utils.exception.CmdException;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ListUtil;
import com.newland.property.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@NewlandPropertyCmd(serviceCode = "work.queryTaskWork")
public class QueryTaskWorkCmd extends Cmd {

    @Autowired
    private IWorkPoolV1InnerServiceSMO workPoolV1InnerServiceSMOImpl;

    @Autowired
    private IWorkCopyV1InnerServiceSMO workCopyV1InnerServiceSMOImpl;

    @Override
    public void validate(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        super.validatePageInfo(reqJson);
        String storeId = CmdContextUtils.getStoreId(context);
        reqJson.put("storeId", storeId);
    }

    @Override
    public void doCmd(CmdEvent event, ICmdDataFlowContext context, JSONObject reqJson) throws CmdException, ParseException {
        WorkPoolDto workPoolDto = BeanConvertUtil.covertBean(reqJson, WorkPoolDto.class);
        String userId = CmdContextUtils.getUserId(context);
        workPoolDto.setStaffId(userId);
        int count = workPoolV1InnerServiceSMOImpl.queryTaskWorkPoolsCount(workPoolDto);
        List<WorkPoolDto> workPoolDtos = null;
        if (count > 0) {
            workPoolDtos = workPoolV1InnerServiceSMOImpl.queryTaskWorkPools(workPoolDto);
        } else {
            workPoolDtos = new ArrayList<>();
        }
        //todo 查询 处理人 和抄送人
        queryCopy(workPoolDtos);
        ResultVo resultVo = new ResultVo((int) Math.ceil((double) count / (double) reqJson.getInteger("row")), count, workPoolDtos);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
        context.setResponseEntity(responseEntity);
    }

    private void queryCopy(List<WorkPoolDto> workPoolDtos) {
        if (ListUtil.isNull(workPoolDtos)) {
            return;
        }
        List<String> workIds = new ArrayList<>();
        for (WorkPoolDto workPoolDto : workPoolDtos) {
            workIds.add(workPoolDto.getWorkId());
        }
        WorkCopyDto workCopyDto = new WorkCopyDto();
        workCopyDto.setWorkIds(workIds.toArray(new String[workIds.size()]));
        List<WorkCopyDto> workCopyDtos = workCopyV1InnerServiceSMOImpl.queryWorkCopys(workCopyDto);
        String curCopyName = "";
        for (WorkPoolDto workPoolDto : workPoolDtos) {
            curCopyName = "";
            for (WorkCopyDto tmpWorkCopyDto : workCopyDtos) {
                if (!WorkTaskDto.STATE_WAIT.equals(tmpWorkCopyDto.getState())) {
                    continue;
                }
                if (!workPoolDto.getWorkId().equals(tmpWorkCopyDto.getWorkId())) {
                    continue;
                }
                if (curCopyName.split(",").length > 2) {
                    continue;
                }
                curCopyName += (tmpWorkCopyDto.getStaffName() + ",");
            }
            workPoolDto.setCurCopyName(curCopyName);
        }
    }
}
