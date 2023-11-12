package com.api.gestion.jwt;

import com.api.gestion.entities.User;
import com.api.gestion.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;

@Slf4j
@Service
public class CustomerDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private User userDetail;

    //vamos a cargar un ussuario por su username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Dentro de loasUserByUsername {}", username);
        userDetail = userRepository.findByEmail(username);//obtenemos el usuario por email
        if (!Objects.isNull(username)){
            return  new org.springframework.security.core.userdetails.User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
        }else{
            throw  new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    public User getUserDeatail(){
        return userDetail;
    }

}
