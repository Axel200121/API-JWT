package com.api.gestion.services;

import org.springframework.http.ResponseEntity;

import java.util.Map;


public interface CategoryService {

    ResponseEntity<String> saveCategory(Map<String,String> requestMap);
}
