package com.forohubalura.forohubalura.configuraciones;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.forohubalura.forohubalura.modelo.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.jwt.secret}")
    private String secret;

    @Value("${api.security.jwt.expiration}")
    private Long duracion;

    public String generarToken(Usuario usuario){
        try{
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create().withIssuer("API FORO HUB ALURA")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(fechaExpiracion())
                    .sign(algoritmo);
        }catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el Token JWT ", exception);
        }
    }

    private Instant fechaExpiracion() {
        return LocalDateTime.now().plusHours(duracion).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT){
        try{
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo).withIssuer("API FORO HUB ALURA")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        }catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT invalido o expirado!");
        }
    }
}
