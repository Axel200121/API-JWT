package com.api.gestion.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
//Cuando se invoca la funcion en el repository user hace la consulta
//@NamedQuery(name = "User.findByEmail", query = "select u from User u where u.email=:email")
@NamedQuery(name = "User.getAllUsers", query = "select new com.api.gestion.dto.UserDTO(u.id, u.nombre, u.email, u.numeroContacto, u.role ,u.status) from User u where u.role='user'")
@NamedQuery(name = "User.updateStatus", query = "update User u set u.status=:status where u.id=:id")
@NamedQuery(name = "User.getAllAdmins", query = "select u.email from User u where u.role='admin'")

/*.........................................................................................*/

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "users")
public class User implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "numeroContacto")
    private String numeroContacto;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "role")
    private String role;
}
