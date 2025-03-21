package com.example.demo.Controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.demo.Dtos.UserLoginDto;
import com.example.demo.Entities.User;
import com.example.demo.Enum.UserType;
import com.example.demo.Repositorys.UserRepository;
import com.example.demo.Services.CustomUserDetailsService;
import com.example.demo.TokenService.GeneretedToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @MockitoBean
    private GeneretedToken tokenService;

    @InjectMocks
    private UserController userController;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private DaoAuthenticationProvider daoAuthenticationProvider;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testLogin_ComCredenciaisValidas_RetornaTokenComSucesso() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("testUser", "testPassword");

        User user = new User();
        user.setName(userLoginDto.name());
        user.setPassword(userLoginDto.password());
        user.setRole(UserType.USER);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        String token = "mocked-jwt-token";
        when(tokenService.generateToken(user)).thenReturn(token);
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("token " + token));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(user);
        assertEquals(user, authentication.getPrincipal());
        assertEquals(true, authentication.isAuthenticated());
        assertEquals(token, tokenService.generateToken(user));
        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void testLogin_ComCredenciaisInvalidas_RetornaUnauthorized() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("testUser", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isForbidden());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(tokenService);
    }
}
