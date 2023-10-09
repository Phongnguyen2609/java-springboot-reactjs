package com.example.fai_edu_classroom.controller;

import com.example.fai_edu_classroom.dto.request.LoginRequest;
import com.example.fai_edu_classroom.dto.request.RegisterRequest;
import com.example.fai_edu_classroom.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUse(@RequestBody LoginRequest loginRequest){
        return authService.authenticateUser(loginRequest);
    }

    // teacher or admin
    @PostMapping("/register")
    public ResponseEntity<?> registerAccount(@Validated @RequestBody RegisterRequest registerRequest){
        return authService.register(registerRequest);
    }
}
