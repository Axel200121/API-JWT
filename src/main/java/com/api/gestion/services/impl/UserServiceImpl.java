package com.api.gestion.services.impl;


import com.api.gestion.contants.FacturaContants;
import com.api.gestion.entities.User;
import com.api.gestion.repositories.UserRepository;
import com.api.gestion.services.UserService;
import com.api.gestion.util.FacturaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Registro interno de un usuario {}", requestMap);
        try{
            if (validateSignMap(requestMap)){
                User user = userRepository.findByEmail(requestMap.get("email"));
                if (Objects.isNull(user)){
                    userRepository.save(getUserFromMap(requestMap));
                    return FacturaUtils.getResponseEntity("Usuario regsitrado con éxito", HttpStatus.CREATED);
                }else{
                    return FacturaUtils.getResponseEntity("El usuario con ese email ya existe", HttpStatus.BAD_REQUEST);
                }
            }else{
                return FacturaUtils.getResponseEntity(FacturaContants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSignMap(Map<String, String> requestMap){
        if (requestMap.containsKey("nombre") && requestMap.containsKey("numeroContacto") &&
                requestMap.containsKey("email") && requestMap.containsKey("password")){
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap){
        User user = new User();
        user.setNombre(requestMap.get("nombre"));
        user.setNumeroContacto(requestMap.get("numeroContacto"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}
