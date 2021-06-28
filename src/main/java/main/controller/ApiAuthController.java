package main.controller;

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
/*
    UserRepository userRepository;

    @GetMapping(value = "/check")
    public ResponseEntity checkUser() {
        User userExists = userRepository.checkUser();

        if (userExists != null) {
            return new ResponseEntity<>(userExists, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

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
