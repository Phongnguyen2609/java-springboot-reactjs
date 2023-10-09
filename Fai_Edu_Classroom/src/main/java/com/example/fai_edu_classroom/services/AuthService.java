package com.example.fai_edu_classroom.services;

import com.example.fai_edu_classroom.dto.request.LoginRequest;
import com.example.fai_edu_classroom.dto.request.RegisterRequest;
import com.example.fai_edu_classroom.dto.response.JwtResponse;
import com.example.fai_edu_classroom.dto.response.MessageResponse;
import com.example.fai_edu_classroom.entity.Account;
import com.example.fai_edu_classroom.repository.IAccountRepository;
import com.example.fai_edu_classroom.security.jwt.JwtUtils;
import com.example.fai_edu_classroom.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    IAccountRepository iAccountRepository;

    @Autowired
    JwtUtils jwtUtils;

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken obj = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        Authentication authentication = authenticationManager.authenticate(obj);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername()
        ));
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest) {
        if (iAccountRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("User name is already taken"));
        }
        Account account = new Account();
        account.setUsername(registerRequest.getUsername());
        if (account.getUsername().trim().length() >= 5 && (account.getUsername().contains(" "))){
            return ResponseEntity.badRequest().body(new MessageResponse("username must greate than or equal to 5 and no space"));
        }
        account.setPassword(encoder.encode(registerRequest.getPassword()));
        iAccountRepository.save(account);

        return ResponseEntity.ok(new MessageResponse("registered successfully!"));
    }
}
