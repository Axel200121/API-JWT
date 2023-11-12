package com.api.gestion.rest;

import com.api.gestion.contants.FacturaContants;
import com.api.gestion.dto.UserDTO;
import com.api.gestion.services.UserService;
import com.api.gestion.util.FacturaUtils;
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
    public ResponseEntity<String> registerUser(@RequestBody(required = true)Map<String,String> requestMap){
        try {
            return userService.signUp(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody(required = true)  Map<String,String> requestMap){
        try {
            return  userService.login(requestMap);
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaContants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        try {
            return  userService.getAllUsers();
        }catch (Exception exception){
            exception.printStackTrace();
        }
        return  new ResponseEntity<List<UserDTO>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
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
}
