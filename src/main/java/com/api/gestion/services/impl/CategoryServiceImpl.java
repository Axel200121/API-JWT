package com.api.gestion.services.impl;

import com.api.gestion.contants.FacturaContants;
import com.api.gestion.entities.Category;
import com.api.gestion.repositories.CategoryRepository;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.services.CategoryService;
import com.api.gestion.util.FacturaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JwtFilter jwtFilter;


    @Override
    public ResponseEntity<String> saveCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                //creamos un metodo para validar la categoria
                if (validateCategoryMap(requestMap,false)){ // se pasa el false porque no contiene el id
                    categoryRepository.save(getCategoryFromMap(requestMap,false));
                    return FacturaUtils.getResponseEntity("Categoria agregada con éxito",HttpStatus.OK);
                }

            }else {
                return FacturaUtils.getResponseEntity(FacturaContants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String,String> requestMap, boolean validateId){
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

    private Category getCategoryFromMap(Map<String,String> requestMap, Boolean isAdd){
        Category  category  = new Category();
        if (isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setNombre(requestMap.get("nombre"));
        return category;
    }
}
