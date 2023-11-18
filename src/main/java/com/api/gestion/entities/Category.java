package com.api.gestion.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


@NamedQuery(name="Category.getAllCategories", query = "select c from Category c")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private  String nombre;


}
