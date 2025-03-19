package com.example.demo.TokenService;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.example.demo.Entities.User;

@Service
public class GeneretedToken {

    String secret = "secret";
    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("SpringSecurity")
                    .withSubject(user.getName())
                    .withExpiresAt(new java.util.Date(System.currentTimeMillis() + 1800000))
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            System.out.println("deu ruim ai ");
            return null;
        }
    }

    public String getSubject(String token) {
        DecodedJWT decodedJWT;
        try {
      
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("SpringSecurity")  
                    .build();

            
            decodedJWT = verifier.verify(token);

      
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println("Erro na verificação do token: " + exception.getMessage());
            return null;
        }
    }
   

}
