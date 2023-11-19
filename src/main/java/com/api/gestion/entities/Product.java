package com.api.gestion.entities;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Product.getAllProducts", query ="select new com.api.gestion.dto.ProductDTO(p.id,p.nombre,p.descripcion,p.precio,p.status, p.category.id, p.category.nombre) from Product p" )

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    //Lazzy -> me va listar la categoria cuando yo se lo pida
    //eagger -> me traer la categoria en automatico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idCategory", nullable = false)
    private Category category;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "status")
    private String status;
}
