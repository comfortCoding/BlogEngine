package main.controller;

import main.api.response.CheckResponse;
import main.services.ApiAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    private final ApiAuthService apiAuthService;

    public ApiAuthController(ApiAuthService apiAuthService) {
        this.apiAuthService = apiAuthService;
    }

    @GetMapping(value = "/check")
    public ResponseEntity<CheckResponse> checkUser() {
        return apiAuthService.checkUser();
    }

    /*
    @GetMapping(value = "/captcha")
    public ResponseEntity<?> setCaptcha() {

        if (true) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    */

}
