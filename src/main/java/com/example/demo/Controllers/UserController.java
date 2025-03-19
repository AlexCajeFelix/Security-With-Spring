package com.example.demo.Controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.demo.Dtos.UserDto;
import com.example.demo.Dtos.UserResponse;
import com.example.demo.Entities.User;
import com.example.demo.Enum.UserType;
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

    @PostMapping("/login")
    public ResponseEntity<String> login() {
       User user = new User( null, "alex123", "123", "123456789", UserType.USER);
        String token = tokenService.generateToken(user);
        

        return ResponseEntity.ok(token);
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
