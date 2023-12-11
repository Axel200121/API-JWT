package com.api.gestion.repositories;

import com.api.gestion.dto.UserDTO;
import com.api.gestion.dto.UserListDTO;
import com.api.gestion.entities.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value = "SELECT * FROM users WHERE email=?1", nativeQuery = true)
    User findByEmail(String email);

    List<UserListDTO> getAllUsers();

    @Transactional
    @Modifying //sirve para indicar que en el metodo van hacer operaciones insr,update y delete, no son de tipo get
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    List<String> getAllAdmins();

}
