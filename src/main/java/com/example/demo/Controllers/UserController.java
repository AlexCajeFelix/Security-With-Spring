package com.example.demo.Controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.example.demo.Dtos.UserDto;
import com.example.demo.Dtos.UserLoginDto;
import com.example.demo.Dtos.UserResponse;
import com.example.demo.Entities.User;

import com.example.demo.Repositorys.UserRepository;
import com.example.demo.TokenService.GeneretedToken;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private GeneretedToken tokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserLoginDto userLoginDto)  {

        try {
         
            System.out.println("Login Request - User: " + userLoginDto.name());
    
            var authenticationToken = new UsernamePasswordAuthenticationToken(userLoginDto.name(), userLoginDto.password());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
    
           
            System.out.println("Authentication Result: " + authentication);

            String token = tokenService.generateToken((User) authentication.getPrincipal());
    
        
            System.out.println("Generated Token: " + token);
    
            return ResponseEntity.ok("token " + token);
    
        } catch (BadCredentialsException e) {
            
            System.out.println("Authentication Failed: Invalid credentials for " + userLoginDto.name());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid user credentials");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> getlogin() { 

        return ResponseEntity.ok("GET Login realizado com sucesso");
    }

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@RequestBody UserDto userDto, ServletUriComponentsBuilder uriBuilder) {
       
        String encryptedPassword = passwordEncoder.encode(userDto.password());

        User user = new User(null, userDto.name(), encryptedPassword, userDto.document(), userDto.role());
       
        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(user.getId())
        .toUri();
        
        UserResponse userResponseDto = new UserResponse(user.getId(), user.getName(), encryptedPassword, user.getDocument(), user.getRole());


    return ResponseEntity.created(location).body(userResponseDto);  
    }
    
}
