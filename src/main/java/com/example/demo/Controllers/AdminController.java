package com.example.demo.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

        @PostMapping
        public ResponseEntity<String> Admin() {

            

        return ResponseEntity.ok("admin realizado com sucesso");
        }
}
