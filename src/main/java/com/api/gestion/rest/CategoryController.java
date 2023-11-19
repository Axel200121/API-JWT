package com.api.gestion.rest;

import com.api.gestion.contants.FacturaContants;
import com.api.gestion.entities.Category;
import com.api.gestion.services.CategoryService;
import com.api.gestion.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/register-category")
    public ResponseEntity<String> saveCategory(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return  categoryService.saveCategory(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-categories")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String valueFilter){
        try {
            return categoryService.getAllCategories(valueFilter);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/update-category")
    public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return  categoryService.updateCategory(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
