package main.services;

import main.api.response.CheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthService {

    public ResponseEntity<CheckResponse> checkUser(){

        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setResult(false);

        return ResponseEntity
                .ok(checkResponse);
    }
}
