package main.controller;

import main.api.request.LoginRequest;
import main.api.response.*;
import main.api.request.RegisterRequest;
import main.services.ApiAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthService apiAuthService;

    public ApiAuthController(ApiAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    @GetMapping(value = "/check")
    public ResponseEntity<LoginResponse> checkUser(Principal principal) {
        LoginResponse response = apiAuthService.checkUser(principal);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/captcha")
    public ResponseEntity<CaptchaResponse> getCaptcha() {
        CaptchaResponse response = apiAuthService.getCaptcha();
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request){
        RegisterResponse response = apiAuthService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request){
        LoginResponse response = apiAuthService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<LogoutResponse> logoutUser(){
        LogoutResponse response = apiAuthService.logoutUser();
        return ResponseEntity.ok(response);
    }
}
