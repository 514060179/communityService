package com.newland.property.job.task.fee;

import com.newland.property.core.factory.GenerateCodeFactory;
import com.newland.property.core.smo.IComputeFeeSMO;
import com.newland.property.dto.room.RoomDto;
import com.newland.property.dto.community.CommunityDto;
import com.newland.property.dto.contract.ContractDto;
import com.newland.property.dto.fee.FeeAttrDto;
import com.newland.property.dto.fee.FeeConfigDto;
import com.newland.property.dto.fee.FeeDto;
import com.newland.property.dto.log.LogSystemErrorDto;
import com.newland.property.dto.owner.OwnerCarDto;
import com.newland.property.dto.owner.OwnerDto;
import com.newland.property.dto.task.TaskDto;
import com.newland.property.intf.common.ILogSystemErrorInnerServiceSMO;
import com.newland.property.intf.community.IParkingSpaceInnerServiceSMO;
import com.newland.property.intf.community.IRoomInnerServiceSMO;
import com.newland.property.intf.fee.IFeeAttrInnerServiceSMO;
import com.newland.property.intf.fee.IFeeConfigInnerServiceSMO;
import com.newland.property.intf.fee.IFeeInnerServiceSMO;
import com.newland.property.intf.store.IContractInnerServiceSMO;
import com.newland.property.intf.user.IOwnerCarInnerServiceSMO;
import com.newland.property.intf.user.IOwnerInnerServiceSMO;
import com.newland.property.job.quartz.TaskSystemQuartz;
import com.newland.property.po.fee.FeeAttrPo;
import com.newland.property.po.log.LogSystemErrorPo;
import com.newland.property.service.smo.ISaveSystemErrorSMO;
import com.newland.property.utils.util.BeanConvertUtil;
import com.newland.property.utils.util.ExceptionUtil;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName GenerateOwnerBillTemplate
 * @Description TODO  矫正数据处理类
 * @Author wuxw
 * @Date 2020/6/4 8:33
 * @Version 1.0
 * add by wuxw 2020/6/4
 **/
@Component
public class CorrectionDataTemplate extends TaskSystemQuartz {

    public static final double DEFAULT_ROW = 1000.0;


    @Autowired
    private IFeeConfigInnerServiceSMO feeConfigInnerServiceSMOImpl;

    @Autowired
    private IFeeInnerServiceSMO feeInnerServiceSMOImpl;

    @Autowired
    private IFeeAttrInnerServiceSMO feeAttrInnerServiceSMOImpl;

    @Autowired
    private IRoomInnerServiceSMO roomInnerServiceSMOImpl;

    @Autowired
    private IOwnerInnerServiceSMO ownerInnerServiceSMOImpl;

    @Autowired
    private IParkingSpaceInnerServiceSMO parkingSpaceInnerServiceSMOImpl;

    @Autowired
    private IOwnerCarInnerServiceSMO ownerCarInnerServiceSMOImpl;


    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Autowired
    private IComputeFeeSMO computeFeeSMOImpl;

    @Autowired
    private ILogSystemErrorInnerServiceSMO logSystemErrorInnerServiceSMOImpl;

    @Autowired
    private ISaveSystemErrorSMO saveSystemErrorSMOImpl;

    @Override
    protected void process(TaskDto taskDto) throws Exception {

        // 获取小区
        List<CommunityDto> communityDtos = getAllCommunity();

        for (CommunityDto communityDto : communityDtos) {
            correctionDataFee(taskDto, communityDto);
        }

    }

    /**
     * 根据小区生成账单
     *
     * @param communityDto
     */
    private void correctionDataFee(TaskDto taskDto, CommunityDto communityDto) {

        //查询费用项
        FeeConfigDto feeConfigDto = new FeeConfigDto();
        feeConfigDto.setCommunityId(communityDto.getCommunityId());

        List<FeeConfigDto> feeConfigDtos = feeConfigInnerServiceSMOImpl.queryFeeConfigs(feeConfigDto);

        for (FeeConfigDto tmpFeeConfigDto : feeConfigDtos) {
            try {
                GenerateOweFeeByFeeConfig(taskDto, tmpFeeConfigDto);
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("矫正数据出错" + tmpFeeConfigDto.getConfigId(), e);
            }
        }


    }

    /**
     * 按费用项来出账
     *
     * @param taskDto
     * @param feeConfigDto
     */
    private void GenerateOweFeeByFeeConfig(TaskDto taskDto, FeeConfigDto feeConfigDto) throws Exception {

        //当前费用项是否
        FeeDto feeDto = new FeeDto();
        feeDto.setConfigId(feeConfigDto.getConfigId());
        feeDto.setCommunityId(feeConfigDto.getCommunityId());

        //先查询总数
        int count = feeInnerServiceSMOImpl.queryFeesCount(feeDto);

       // double record = Math.ceil(count / DEFAULT_ROW);
        int record = (int) Math.ceil((double) count / (double) DEFAULT_ROW);

        for (int page = 1; page <= record; page++) {
            try {
                feeDto.setPage(page);
                feeDto.setRow(new Double(DEFAULT_ROW).intValue());
                List<FeeDto> feeDtos = feeInnerServiceSMOImpl.queryFees(feeDto);
                //没有关联费用
                if (feeDtos == null || feeDtos.size() < 1) {
                    continue;
                }
                for (FeeDto tmpFeeDto : feeDtos) {
                    try {
                        correctionData(tmpFeeDto, feeConfigDto);
                    } catch (Exception e) {
                        LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                        logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                        logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                        logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                        saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                        logger.error("矫正数据出错", e);
                    }
                }
            } catch (Exception e) {
                LogSystemErrorPo logSystemErrorPo = new LogSystemErrorPo();
                logSystemErrorPo.setErrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_errId));
                logSystemErrorPo.setErrType(LogSystemErrorDto.ERR_TYPE_JOB);
                logSystemErrorPo.setMsg(ExceptionUtil.getStackTrace(e));
                saveSystemErrorSMOImpl.saveLog(logSystemErrorPo);
                logger.error("矫正数据出错" + feeConfigDto.getConfigId(), e);
            }
        }

    }

    /**
     * 生成 费用
     *
     * @param feeDto
     */
    private void correctionData(FeeDto feeDto, FeeConfigDto feeConfigDto) {

        //费用 根据房屋 车辆 合同去查询 业主信息

        String objName = "";
        String ownerId = "";
        String tel = "";
        String name = "";

        if (FeeDto.PAYER_OBJ_TYPE_ROOM.equals(feeDto.getPayerObjType())) {
            RoomDto roomDto = new RoomDto();
            roomDto.setRoomId(feeDto.getPayerObjId());
            roomDto.setCommunityId(feeDto.getCommunityId());
            List<RoomDto> roomDtos = roomInnerServiceSMOImpl.queryRooms(roomDto);
            if (roomDtos == null || roomDtos.size() != 1) {
                return;
            }
            roomDto = roomDtos.get(0);
            objName = roomDto.getFloorNum() + "-" + roomDto.getUnitNum() + "-" + roomDto.getRoomNum();

            OwnerDto ownerDto = new OwnerDto();
            ownerDto.setRoomId(roomDto.getRoomId());
            List<OwnerDto> ownerDtos = ownerInnerServiceSMOImpl.queryOwners(ownerDto);

            if (ownerDtos != null && ownerDtos.size() > 0) {
                ownerId = ownerDtos.get(0).getOwnerId();
                tel = ownerDtos.get(0).getLink();
                name = ownerDtos.get(0).getName();
            }
        } else if (FeeDto.PAYER_OBJ_TYPE_CAR.equals(feeDto.getPayerObjType())) {
            OwnerCarDto ownerCarDto = new OwnerCarDto();
            ownerCarDto.setCarId(feeDto.getPayerObjId());
            ownerCarDto.setCommunityId(feeDto.getCommunityId());
            List<OwnerCarDto> ownerCarDtos = ownerCarInnerServiceSMOImpl.queryOwnerCars(ownerCarDto);

            if (ownerCarDtos == null || ownerCarDtos.size() < 1) {
                return;
            }
            objName = ownerCarDtos.get(0).getCarNum();
            ownerId = ownerCarDtos.get(0).getOwnerId();
            tel = ownerCarDtos.get(0).getLink();
            name = ownerCarDtos.get(0).getOwnerName();
        } else if (FeeDto.PAYER_OBJ_TYPE_CONTRACT.equals(feeDto.getPayerObjType())) {


            ContractDto contractDto = new ContractDto();
            contractDto.setContractId(feeDto.getPayerObjId());
            List<ContractDto> contractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

            if (contractDtos == null || contractDtos.size() < 1) {
                return;
            }

            objName = contractDtos.get(0).getContractCode();
            tel = contractDtos.get(0).getbLink();
            name = contractDtos.get(0).getPartyB();
        }

        updateFeeAttr(FeeAttrDto.SPEC_CD_OWNER_ID, ownerId, feeDto);
        updateFeeAttr(FeeAttrDto.SPEC_CD_OWNER_NAME, name, feeDto);
        updateFeeAttr(FeeAttrDto.SPEC_CD_OWNER_LINK, tel, feeDto);
        updateFeeAttr(FeeAttrDto.SPEC_CD_PAY_OBJECT_NAME, objName, feeDto);

    }

    private void updateFeeAttr(String specCd, String value, FeeDto feeDto) {

        if (StringUtil.isEmpty(value)) {
            return;
        }

        FeeAttrPo curFeeAttrPo = null;

        for (FeeAttrDto feeAttrDto : feeDto.getFeeAttrDtos()) {
            if (specCd.equals(feeAttrDto.getSpecCd())) {
                curFeeAttrPo = BeanConvertUtil.covertBean(feeAttrDto, FeeAttrPo.class);
            }
        }

        if (curFeeAttrPo == null) {
            curFeeAttrPo = new FeeAttrPo();
            curFeeAttrPo.setFeeId(feeDto.getFeeId());
            curFeeAttrPo.setCommunityId(feeDto.getCommunityId());
            curFeeAttrPo.setValue(value);
            curFeeAttrPo.setSpecCd(specCd);
            curFeeAttrPo.setAttrId(GenerateCodeFactory.getGeneratorId(GenerateCodeFactory.CODE_PREFIX_attrId));
            List<FeeAttrPo> feeAttrPos = new ArrayList<>();
            feeAttrPos.add(curFeeAttrPo);
            feeAttrInnerServiceSMOImpl.saveFeeAttrs(feeAttrPos);

            return;
        }

        curFeeAttrPo.setValue(value);
        feeAttrInnerServiceSMOImpl.updateFeeAttr(curFeeAttrPo);

    }
}
