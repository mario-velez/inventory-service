package com.acelerati.management_service.application.handler;

import com.acelerati.management_service.application.dto.request.InventoryDTO;

import com.acelerati.management_service.application.dto.request.InventorySearchCriteriaDTO;
import com.acelerati.management_service.application.dto.request.PaginationDTO;
import com.acelerati.management_service.application.dto.response.*;

import com.acelerati.management_service.application.dto.request.InventoryUpdateRequestDTO;


import java.util.List;

public interface InventorySpringService {
    void addInventory(List<InventoryDTO> inventoryDTO);
    FilterInventoryResponseDTO getInventoriesBy(InventorySearchCriteriaDTO searchCriteria, PaginationDTO paginationDTO);
	List<ProductsForSaleDTO> getAllProductForSale(String name,String brandName,String categoryName,int page,int elementsPerPage);
	List<ProductDTO> fetchProductsFromMicroservice(Integer page, Integer itemsNumber);
    List<CategoryDTO> fetchCategoriesFromMicroservice(Integer page, Integer itemsNumber);
    List<BrandDTO> fetchBrandsFromMicroservice(Integer page, Integer itemsNumber);
    void updateProductSalePrice(InventoryUpdateRequestDTO inventoryDTO);

}
