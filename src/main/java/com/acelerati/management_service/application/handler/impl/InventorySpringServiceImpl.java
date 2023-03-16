package com.acelerati.management_service.application.handler.impl;

import com.acelerati.management_service.application.driven.ProductFeignClientPort;
import com.acelerati.management_service.application.dto.request.InventoryDTO;
import com.acelerati.management_service.application.dto.request.InventorySearchCriteriaDTO;
import com.acelerati.management_service.application.dto.request.PaginationDTO;
import com.acelerati.management_service.application.dto.response.*;
import com.acelerati.management_service.application.dto.response.ProductDTO;
import com.acelerati.management_service.application.dto.response.ProductsForSaleDTO;
import com.acelerati.management_service.application.handler.InventorySpringService;
import com.acelerati.management_service.application.mapper.*;
import com.acelerati.management_service.domain.api.InventoryServicePort;
import com.acelerati.management_service.domain.util.InventorySearchCriteriaUtil;
import com.acelerati.management_service.domain.util.PaginationUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InventorySpringServiceImpl implements InventorySpringService {
     private final InventoryServicePort inventoryServicePort;
     private final InventoryRequestMapper inventoryRequestMapper;
     private final InventorySearchMapper inventorySearchMapper;
     private final PaginationRequestMapper paginationRequestMapper;
     private final PaginationResponseMapper paginationResponseMapper;
	 private final ProductFeignClientPort productFeignClientPort;

    public InventorySpringServiceImpl(InventoryServicePort inventoryServicePort, InventoryRequestMapper inventoryRequestMapper, InventorySearchMapper inventorySearchMapper, PaginationRequestMapper paginationRequestMapper, PaginationResponseMapper paginationResponseMapper, ProductFeignClientPort productFeignClientPort) {
        this.inventoryServicePort = inventoryServicePort;
        this.inventoryRequestMapper = inventoryRequestMapper;
        this.inventorySearchMapper = inventorySearchMapper;
        this.paginationRequestMapper = paginationRequestMapper;
        this.paginationResponseMapper = paginationResponseMapper;
        this.productFeignClientPort = productFeignClientPort;
    }

    @Override
     public void addInventory(List<InventoryDTO> inventoryDTO) {
          this.inventoryServicePort.addInventory(this.inventoryRequestMapper.toListModel(inventoryDTO));

     }

     @Override
     public FilterInventoryResponseDTO getInventoriesBy(InventorySearchCriteriaDTO searchCriteriaDTO,
                                                        PaginationDTO paginationDTO) {
          InventorySearchCriteriaUtil inventorySearchCriteriaModel = inventorySearchMapper.toModel(searchCriteriaDTO);
          PaginationUtil paginationModel = paginationRequestMapper.toModel(paginationDTO);

          // Do the query against the database and update the paginator
          List<InventoryResponseDTO> inventoriesResponse =
                  inventorySearchMapper.toDTOList(
                          inventoryServicePort.getInventoriesBy(inventorySearchCriteriaModel, paginationModel));

          // Fetch products from the corresponding microservice
          List<ProductDTO> feignClientResponseDTOS = fetchProductsFromMicroservice();

          PaginationResponseDTO paginationResponse = paginationResponseMapper.toResponseDTO(paginationModel);
          return new FilterInventoryResponseDTO(joinInventoryAndProduct(inventoriesResponse, feignClientResponseDTOS),
                  paginationResponse);
     }
	@Override
    public List<ProductsForSaleDTO> getAllProductForSale(String name, String nombreMarca, String nombreCategoria,int page,int elementPerPage) {
        List<InventoryResponseDTO> inventoryList = this.inventorySearchMapper.toDTOList(this.inventoryServicePort.getAllInventoryWithStockAndSalePriceGreaterThan0());
        List<ProductDTO> products = this.productFeignClientPort.fetchProductsFromMicroservice();
        List<ProductsForSaleDTO> dataFiltered = mergeData(inventoryList, products);
        return dataPaginated(dataFiltered,page,elementPerPage);
    }



    public List<ProductsForSaleDTO>dataPaginated(List<ProductsForSaleDTO> dataFiltered,int page,int elementPerPage){

        return dataFiltered.stream()
                .skip((long) (page - 1) * elementPerPage)
                .limit(elementPerPage)
                .collect(Collectors.toList());
    }

    public List<ProductsForSaleDTO> mergeData(List<InventoryResponseDTO> inventoryList, List<ProductDTO> products) {
        Map<Long, InventoryResponseDTO> dataInventory = inventoryList.stream()
                //Function.identity() is equal to element -> element
                .collect(Collectors.toMap(InventoryResponseDTO::getId, Function.identity()));
        return products.stream()
                .filter(product -> dataInventory.containsKey(Long.valueOf(product.getId())))
                .map(product -> new ProductsForSaleDTO(Long.valueOf(product.getId()),
                        product.getName(),
                        dataInventory.get(Long.valueOf(product.getId())).getSalePrice(),
                        dataInventory.get(Long.valueOf(product.getId())).getStock(),
                        product.getDescription())).collect(Collectors.toList());
    }

    private List<InventoryAndProductResponseDTO> joinInventoryAndProduct(List<InventoryResponseDTO> inventories,
                                                                         List<ProductDTO> products) {
        Map<Long, InventoryResponseDTO> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(InventoryResponseDTO::getId, Function.identity()));
        return products.stream()
                .filter(product -> inventoryMap.containsKey(product.getId()))
                .map(product -> new InventoryAndProductResponseDTO(inventoryMap.get(product.getId()), product))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> fetchProductsFromMicroservice() {
        return productFeignClientPort.fetchProductsFromMicroservice();
    }

}
