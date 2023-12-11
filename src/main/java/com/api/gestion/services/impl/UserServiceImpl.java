package com.api.gestion.services.impl;


import com.api.gestion.contants.FacturaContants;
import com.api.gestion.dto.*;
import com.api.gestion.entities.User;
import com.api.gestion.repositories.UserRepository;
import com.api.gestion.security.CustomerDetailsService;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.security.jwt.JwtUtil;
import com.api.gestion.services.UserService;
import com.api.gestion.util.EmailUtils;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.util.GestionException;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;


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

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private EmailUtils emailUtils;


    @Override
    public ResponseEntity<ResponseDTO> signUp(UserDTO userDTO) {
        log.info("Registro interno de un usuario {}", userDTO);
        try{
            if (validateSignMap(userDTO)){
                User user = userRepository.findByEmail(userDTO.getEmail());
                if (Objects.isNull(user)){
                    userRepository.save(getUserFromMap(userDTO));
                    return GestionException.getResponseEntity("Usuario regsitrado con éxito", HttpStatus.CREATED);
                }else{
                    return GestionException.getResponseEntity("El usuario con ese email ya existe", HttpStatus.BAD_REQUEST);
                }
            }else{
                return GestionException.getResponseEntity(FacturaContants.INVALID_DATA,HttpStatus.BAD_REQUEST);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return GestionException.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ResponseTokenDTO> login(LoginDTO loginDTO) {
        log.info("Dentro del login {}");
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            if (authentication.isAuthenticated()){
                if (customerDetailsService.getUserDeatail().getStatus().equalsIgnoreCase("true")){
                    return GestionException.getResponseEntityToken("Usuario logueado", jwtUtil.generateToken(customerDetailsService.getUserDeatail().getEmail(), customerDetailsService.getUserDeatail().getRole()) , HttpStatus.OK);
                }else{
                    return GestionException.getResponseEntityToken("Espere la aprobacion del administrador","null", HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return GestionException.getResponseEntityToken("Credenciales incorrectas","null", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserListDTO>> getAllUsers() {
        try {
            if (jwtFilter.isAdmin()){
                return  new ResponseEntity<>(userRepository.getAllUsers(),HttpStatus.OK);
            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){

                Optional<User> optionalUser = userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (optionalUser.isPresent()){
                    userRepository.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
                    //sendEmailtoAdmins(requestMap.get("status"), optionalUser.get().getEmail(),userRepository.getAllAdmins());
                    return FacturaUtils.getResponseEntity("status del usuario actualizado",HttpStatus.OK);
                }else{
                    FacturaUtils.getResponseEntity("Este usuario no existe",HttpStatus.NOT_FOUND);
                }

            }else {
                return FacturaUtils.getResponseEntity(FacturaContants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return FacturaUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (!user.equals(null)){
                if (user.getPassword().equals(requestMap.get("oldPassword"))){
                    user.setPassword(requestMap.get("newPassword"));
                    userRepository.save(user);
                    return  FacturaUtils.getResponseEntity("Contraseña actualizada con exito", HttpStatus.OK);
                }
                return FacturaUtils.getResponseEntity("Contraseña incorrecta",HttpStatus.BAD_REQUEST);
            }
            return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.BAD_REQUEST);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userRepository.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())){
                emailUtils.forgotPassword(user.getEmail(),"Credenciales del sistema gestion de facturas", user.getPassword());
            }
            return FacturaUtils.getResponseEntity("Verifica tus credenciales",HttpStatus.OK);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //Funcionalidad para envir correo cuanfo se cambia el status
    /*private void sendEmailtoAdmins(String status, String user,List<String> allAdmins){
        allAdmins.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Cuenta Aprobada","Usuario: "+ user + "\n es aprobado por \nAdmin: "+ jwtFilter.getCurrentUser(), allAdmins);
        }else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Cuenta no aprodada","Usuario: "+ user + "\n es desaprobado por \nAdmin: "+ jwtFilter.getCurrentUser(), allAdmins);
        }

    }*/

    private boolean validateSignMap(UserDTO userDTO){
        if (userDTO.getNombre() != null && userDTO.getNumeroContacto() != null  &&
                userDTO.getEmail() != null  &&  userDTO.getPassword()!= null  ){
            return true;
        }
        return false;
    }

    private User getUserFromMap(UserDTO userDTO){
        User user = new User();
        user.setNombre(userDTO.getNombre());
        user.setNumeroContacto(userDTO.getNumeroContacto());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setStatus("false");
        user.setRole("user");
        return user;
    }
}
