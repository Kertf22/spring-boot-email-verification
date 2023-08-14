package com.example.springemailverification.controllers;


import com.example.springemailverification.DTOS.CreateUserDTO;
import com.example.springemailverification.DTOS.LoginUserDTO;
import com.example.springemailverification.model.HttpResponse;
import com.example.springemailverification.model.User;
import com.example.springemailverification.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createUser(@RequestBody CreateUserDTO body) {
        try {
            var user = this.userService.createUser(body.name(), body.email(), body.password());
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            HttpResponse.builder()
                                    .timestamp(java.time.LocalDateTime.now().toString())
                                    .statusCode(HttpStatus.CREATED.value())
                                    .status(HttpStatus.CREATED)
                                    .message("User created successfully")
                                    .data(Map.of("user", user))
                                    .build()
                    );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    HttpResponse.builder()
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody LoginUserDTO body) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(
                    HttpResponse.builder()
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .message("User logged in successfully")
                            .data(Map.of("user", this.userService.login(body.email(), body.password())))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    HttpResponse.builder()
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<HttpResponse> confirmUser(@RequestParam("token") String token) {
        try {
            this.userService.verifyUser(token);
            return ResponseEntity.status(HttpStatus.OK).body(
                    HttpResponse.builder()
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .message("User verified successfully")
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    HttpResponse.builder()
                            .timestamp(java.time.LocalDateTime.now().toString())
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .status(HttpStatus.BAD_REQUEST)
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
