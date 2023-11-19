package com.api.gestion.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface ProductService {

    ResponseEntity<String> saveProduct(Map<String,String> requestMap);
}
