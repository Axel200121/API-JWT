package com.api.gestion.services;

import com.api.gestion.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ProductService {

    ResponseEntity<String> saveProduct(Map<String,String> requestMap);

    ResponseEntity<List<ProductDTO>> getAllProducts();
}
