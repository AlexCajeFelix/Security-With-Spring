package com.example.demo.Dtos;

import com.example.demo.Enum.UserType;

public record UserResponse(Long id, String name, String password, String document, UserType role) {
}
