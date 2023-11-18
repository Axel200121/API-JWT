package com.api.gestion.services;

import com.api.gestion.entities.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;


public interface CategoryService {

    ResponseEntity<String> saveCategory(Map<String,String> requestMap);

    ResponseEntity<List<Category>> getAllCategories(String valueFilter);
}
