package com.smartshop.product.service;

import java.util.List;

import com.smartshop.product.dto.ProductRequest;
import com.smartshop.product.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse getProductById(Long id);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getProductsByCategory(String category);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}