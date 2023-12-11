package com.api.gestion.services;

import com.api.gestion.dto.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface UserService {

    ResponseEntity<ResponseDTO> signUp(UserDTO userDTO);

    //ResponseEntity<ResponseTokenDTO> login(Map<String,String> rquestMap);

    ResponseEntity<ResponseTokenDTO> login(LoginDTO loginDTO);

    ResponseEntity<List<UserListDTO>> getAllUsers();

    ResponseEntity<String> updateStatus(Map<String,String> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String,String> requestMap);

    ResponseEntity<String> forgotPassword(Map<String,String> requestMap);
}
