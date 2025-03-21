package com.example.demo.Factory;

import com.example.demo.Entities.User;
import com.example.demo.Enum.UserType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@EqualsAndHashCode
public class Factory {
  
    public User createUser() {
        User user = new User();
        user.setId(1L);
        user.setName("alex");
        user.setPassword("123");
        user.setDocument("123456789");
        user.setRole(UserType.ADMIN);
        return user;
}

}