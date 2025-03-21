package com.example.demo.Entities;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.Dtos.UserDto;
import com.example.demo.Enum.UserType;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    

    private String name;
    
    private String password;
    
    private String document;
    @Enumerated(jakarta.persistence.EnumType.STRING)
    @NonNull
    private UserType role;


    public User(UserDto userDto) {
        this.name = userDto.name();
        this.password = userDto.password();
        this.document = userDto.document();
        this.role = userDto.role();
    }

   
  
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       
        return Collections.singletonList(() -> "ROLE_" + role.name());
    }

    @Override
    public String getUsername() {
        return name; 
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  
    }

    @Override
    public boolean isEnabled() {
        return true;  
    }



    public User(long l, String string, String string2, String string3, List<SimpleGrantedAuthority> of) {
    


    }



   
}
