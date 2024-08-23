package org.vladimir.t1.currency.service.api.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;
import org.vladimir.t1.currency.service.api.auth.LoginAndRegistrationAuthentication;
import org.vladimir.t1.currency.service.api.dto.LoginRequest;
import org.vladimir.t1.currency.service.api.dto.RegistrationRequest;
import org.vladimir.t1.currency.service.api.entity.User;
import org.vladimir.t1.currency.service.api.service.UserService;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @PostMapping("")
    public void registerUser(@RequestBody  final RegistrationRequest registrationRequest) {
        log.info("Registering user {}", registrationRequest);
        var registrationResponce = userService.registerUser(registrationRequest);
        log.info("Registered user {}", registrationResponce);
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new LoginAndRegistrationAuthentication(registrationResponce.getRole().name(),
                        registrationResponce.getUsername(),
                        registrationResponce.getId()
                )));

    }

    @PostMapping("me/login")
    public void login(@RequestBody  LoginRequest loginRequest) {
      var login = userService.loginUser(loginRequest);
        SecurityContextHolder.setContext(new SecurityContextImpl(
                new LoginAndRegistrationAuthentication(login.getRole(),
                        login.getUsername(),
                        login.getId()
                )));

    }

    @GetMapping("me/hello")
    public ResponseEntity<Map<String,String>> getString() {
        return ResponseEntity.ok(Map.of("hello","hello"));
    }


}
