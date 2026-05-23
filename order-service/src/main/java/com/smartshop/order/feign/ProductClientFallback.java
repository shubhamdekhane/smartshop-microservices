package com.smartshop.order.feign;

import org.springframework.stereotype.Component;
import com.smartshop.order.dto.ProductResponse;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public ProductResponse getProductById(Long id) {
    	return new ProductResponse(id, "Product unavailable", "N/A", 0.0, 0, "N/A");
    }
}