package com.newland.property.report.bmo.search.impl;

import com.newland.property.dto.contract.ContractDto;
import com.newland.property.dto.data.SearchDataDto;
import com.newland.property.intf.store.IContractInnerServiceSMO;
import com.newland.property.report.bmo.search.ISearchContractBMO;
import com.newland.property.utils.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchContractBMOImpl implements ISearchContractBMO {

    @Autowired
    private IContractInnerServiceSMO contractInnerServiceSMOImpl;

    @Override
    public SearchDataDto query(SearchDataDto searchDataDto) {

        List<ContractDto> contractDtos = new ArrayList<>();
        // todo 根据名称查询合同
        queryContractByName(searchDataDto, contractDtos);

        // todo 根据手机号查询合同
        queryContractByLink(searchDataDto, contractDtos);

        searchDataDto.setContracts(contractDtos);

        return searchDataDto;
    }

    /**
     * 根据手机号 查询 合同
     * @param searchDataDto
     * @param contractDtos
     */
    private void queryContractByLink(SearchDataDto searchDataDto, List<ContractDto> contractDtos) {

        if(StringUtil.isEmpty(searchDataDto.getTel())){
            return;
        }

        ContractDto contractDto = new ContractDto();
        contractDto.setPartyB(searchDataDto.getTel());
        contractDto.setStoreId(searchDataDto.getStoreId());
        List<ContractDto> tmpContractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        if (tmpContractDtos == null || tmpContractDtos.size() < 1) {
            return;
        }

        contractDtos.addAll(tmpContractDtos);
    }

    private void queryContractByName(SearchDataDto searchDataDto, List<ContractDto> contractDtos) {

        ContractDto contractDto = new ContractDto();
        contractDto.setContractNameLike(searchDataDto.getSearchValue());
        contractDto.setStoreId(searchDataDto.getStoreId());
        List<ContractDto> tmpContractDtos = contractInnerServiceSMOImpl.queryContracts(contractDto);

        if (tmpContractDtos == null || tmpContractDtos.size() < 1) {
            return;
        }

        contractDtos.addAll(tmpContractDtos);
    }
}
