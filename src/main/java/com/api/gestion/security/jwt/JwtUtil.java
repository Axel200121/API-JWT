package com.api.gestion.security.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

//va tener metodos necesarios para generar, validar tel token, va extraer información del token
@Service
public class JwtUtil {

    private String secret = "springboot"; //clave secreta del jwt

    public String extractUserName(String token){
        //Los claims pueden tener el username, la fecha de caducidad, etc
        //extraer el username
        return extractClient(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        //extrae la fecha de expiracion
        return  extractClient(token,Claims::getExpiration);
    }

    public <T> T extractClient(String token, Function<Claims,T> claimsRsolver){
        final Claims claims = extractAllClaims(token);
        return claimsRsolver.apply(claims);
    }

    public Claims extractAllClaims(String token){
        //este paso lo que hace es firmar el token
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //Nos va permitir verificar si el token es vallido
    private  Boolean isTokenExpire(String token){
        return extractExpiration(token).before(new Date());
    }

    //va generar el token de accesp
    public String generateToken(String username, String role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role",role);
        return  createToken(claims,username);
    }

    private String createToken(Map<String,Object> claims, String username){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 100 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256,secret).compact(); //seleccionamos el algoritmo de ecriptacion y firmamos con la llave secreta
    }

    public Boolean validateToken(String token, UserDetails  userDetails){
        final String username = extractUserName(token);
        //si este token aun no expira y es el mismo usuario el token es valido
        return (username.equals(userDetails.getUsername()) && !isTokenExpire(token));
    }

}
