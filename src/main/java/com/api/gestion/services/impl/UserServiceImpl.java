package com.api.gestion.services.impl;


import com.api.gestion.contants.FacturaContants;
import com.api.gestion.entities.User;
import com.api.gestion.repositories.UserRepository;
import com.api.gestion.security.CustomerDetailsService;
import com.api.gestion.security.jwt.JwtUtil;
import com.api.gestion.services.UserService;
import com.api.gestion.util.FacturaUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager; //quien puede acceder

    @Autowired
    private CustomerDetailsService customerDetailsService;

    @Autowired
    private JwtUtil jwtUtil;


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

    @Override
    public ResponseEntity<String> login(Map<String, String> rquestMap) {
        log.info("Dentro del login {}");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(rquestMap.get("email"), rquestMap.get("password"))
            );
            if (authentication.isAuthenticated()){
                if (customerDetailsService.getUserDeatail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtil.generateToken(customerDetailsService.getUserDeatail().getEmail(), customerDetailsService.getUserDeatail().getRole()) + "\"}", HttpStatus.OK);
                }else{
                    return new ResponseEntity<String>("{\"mensaje\":\""+"Espere la aprobacion del administrador"+"\"}",HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<String>("{\"mensaje\":\""+"Credenciales incorrectas"+"\"}",HttpStatus.BAD_REQUEST);
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
