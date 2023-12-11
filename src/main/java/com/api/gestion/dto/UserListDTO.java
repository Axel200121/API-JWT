package com.api.gestion.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserListDTO {

    private Integer id;
    private String nombre;
    private String email;
    private String numeroContacto;
    private String role;
    private String status;

}
