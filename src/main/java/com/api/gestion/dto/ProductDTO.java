package com.api.gestion.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Integer id;
    private String nombre;
    private  String descripcion;
    private Integer precio;
    private String status;
    private Integer idCategory;
    private String nombreCategory;
}
