package com.api.gestion.rest;

import com.api.gestion.contants.FacturaContants;
import com.api.gestion.dto.*;
import com.api.gestion.services.UserService;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.util.GestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody(required = true) UserDTO userDTO){
        try {
            return userService.signUp(userDTO);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return GestionException.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseTokenDTO> login(@RequestBody(required = true)LoginDTO loginDTO){
        try {
            return  userService.login(loginDTO);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return GestionException.getResponseEntityToken(FacturaContants.SOMETHING_WENT_WRONG,"null",HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UserListDTO>> getAllUsers(){
        try {
            return  userService.getAllUsers();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  new ResponseEntity<List<UserListDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update-status")
    public ResponseEntity<String> updateStatus(@RequestBody(required = true) Map<String,String> requestMap){
        try {
            return userService.updateStatus(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/check-token")
    public ResponseEntity<String> validateToken(){
        try{
            return userService.checkToken();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody(required = true)  Map<String,String> requestMap){
        try{
            return userService.changePassword(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<String> recuperarPassword(@RequestBody Map<String,String> requestMap){
        try{
            return userService.forgotPassword(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
