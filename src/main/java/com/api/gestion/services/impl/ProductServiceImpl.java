package com.api.gestion.services.impl;

import com.api.gestion.contants.FacturaContants;
import com.api.gestion.dto.ProductDTO;
import com.api.gestion.entities.Category;
import com.api.gestion.entities.Product;
import com.api.gestion.repositories.ProductRepository;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.services.ProductService;
import com.api.gestion.util.FacturaUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> saveProduct(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (validateProductMap(requestMap,false)){ // se pasa el false porque no contiene el id
                    productRepository.save(getProductFromMap(requestMap,false));
                    return FacturaUtils.getResponseEntity("Producto agregado con éxito", HttpStatus.OK);
                }
                return FacturaUtils.getResponseEntity(FacturaContants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }else {
                return FacturaUtils.getResponseEntity(FacturaContants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
           return new ResponseEntity<>(productRepository.getAllProducts(),HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Product getProductFromMap(Map<String,String> requestMap, boolean isAdd){
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("idCategory")));
        Product product = new Product();
        if (isAdd){
            product.setId(Integer.parseInt(requestMap.get("id")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setNombre(requestMap.get("Nombre"));
        product.setDescripcion(requestMap.get("descripcion"));
        product.setPrecio(Integer.parseInt(requestMap.get("precio")));
        return product;
    }

    private boolean  validateProductMap(Map<String,String> requestMap, boolean validateId){
        //TODO: este metodo es para actualizar un valor
        if (requestMap.containsKey("nombre")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            }
            if (!validateId){
                return true;
            }
        }
        return false;
    }
}
