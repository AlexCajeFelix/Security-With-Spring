package com.example.demo.TokenService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;


import com.example.demo.Entities.User;
import com.example.demo.Factory.Factory;


public class GeneretedTokenTest {

    @InjectMocks
   private GeneretedToken generetedToken;

    @Mock
    private AuthenticationManager authenticationManager;


    @BeforeEach
    void setUp() {
          MockitoAnnotations.openMocks(this);
    }

    @Test
    void ShoulCreateTkenWhensUserIsValid() {
        User user = new Factory().createUser();
        String name = user.getName();
        String token = generetedToken.generateToken(user);      
        assertNotNull(generetedToken.generateToken(user), "Token não pode ser nulo");
        assertEquals(name, generetedToken.getSubject(token), "Token gerado com sucesso e com o sujeito correto");  
    }
     @Test
    void shouldReturnNullWhenTokenIsInvalid() {       
        String invalidToken = "invalid.token.here";
        String subject = generetedToken.getSubject(invalidToken);
        assertNull(subject, "O token inválido não deve retornar sujeito");
    }



 

}
