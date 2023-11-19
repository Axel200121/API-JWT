package com.api.gestion.rest;


import com.api.gestion.contants.FacturaContants;
import com.api.gestion.dto.ProductDTO;
import com.api.gestion.services.ProductService;
import com.api.gestion.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/register-product")
    public ResponseEntity<String> saveProduct(@RequestBody Map<String,String> requestMap){
        try {
            return  productService.saveProduct(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-products")
    public ResponseEntity<List<ProductDTO>> getAllProducts(){
        try {
            return productService.getAllProducts();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
