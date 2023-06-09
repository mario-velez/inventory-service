package com.acelerati.management_service.application.mapper;

import com.acelerati.management_service.application.dto.request.InventoryDTO;
import com.acelerati.management_service.domain.model.InventoryModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface InventoryResponseMapper {
    InventoryDTO toDTO(InventoryModel inventoryModel);
    List<InventoryDTO> toListDTO(List<InventoryModel> inventoryModels);
}
