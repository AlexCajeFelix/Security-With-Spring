package com.example.demo.Dtos;

import com.example.demo.Enum.UserType;

public record UserDto(String name, String password, String document, UserType role) {
} 
