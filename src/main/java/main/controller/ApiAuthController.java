package main.controller;

import main.api.response.CheckResponse;
import main.api.response.InitResponse;
import main.model.User;
import main.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @GetMapping(value = "/check")
    public ResponseEntity<CheckResponse> checkUser() {

        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setResult(false);

        return ResponseEntity
                .ok(checkResponse);
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
