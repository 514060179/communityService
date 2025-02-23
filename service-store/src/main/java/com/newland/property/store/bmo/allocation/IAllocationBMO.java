package com.newland.property.store.bmo.allocation;

import com.newland.property.dto.purchase.AllocationStorehouseDto;

public interface IAllocationBMO {

    /**
     * 调拨
     * @param tmpAllocationStorehouseDto
     * @param allocationStock
     */
    void doToAllocationStorehouse(AllocationStorehouseDto tmpAllocationStorehouseDto, double allocationStock);
}
