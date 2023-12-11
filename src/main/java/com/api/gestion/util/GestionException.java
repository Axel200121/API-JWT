package com.api.gestion.util;

import com.api.gestion.dto.ResponseDTO;
import com.api.gestion.dto.ResponseTokenDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class GestionException {

    private GestionException(){}

    public static ResponseEntity<ResponseDTO> getResponseEntity(String message, HttpStatus httpStatus) {
        ResponseDTO response = new ResponseDTO(httpStatus.value(), message);
        return new ResponseEntity<>(response, httpStatus);
    }

    public static ResponseEntity<ResponseTokenDTO> getResponseEntityToken(String message, String token, HttpStatus httpStatus) {
        ResponseTokenDTO response = new ResponseTokenDTO(httpStatus.value(), message, token);
        return new ResponseEntity<>(response, httpStatus);
    }


}
