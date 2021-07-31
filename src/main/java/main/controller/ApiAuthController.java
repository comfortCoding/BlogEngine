package main.controller;

import main.api.response.CaptchaResponse;
import main.api.response.CheckResponse;
import main.api.request.RegisterRequest;
import main.api.response.RegisterResponse;
import main.services.ApiAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthService apiAuthService;

    public ApiAuthController(ApiAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    @GetMapping(value = "/check")
    public ResponseEntity<CheckResponse> checkUser() {
        CheckResponse response = apiAuthService.checkUser();
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
    public ResponseEntity<?> loginUser(){
        return ResponseEntity.ok(null);
    }

    @GetMapping(value = "/logout")
    public ResponseEntity<?> logoutUser(){
        return ResponseEntity.ok(null);
    }
}
