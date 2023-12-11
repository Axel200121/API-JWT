package com.api.gestion.dto;

import lombok.Data;

@Data
public class ResponseDTO {

    private int statusCode;
    private String message;

    public ResponseDTO(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    // Getters y Setters
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
