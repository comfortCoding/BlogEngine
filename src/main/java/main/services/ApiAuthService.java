package main.services;

import main.api.response.CheckResponse;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthService {

    public CheckResponse checkUser() {

        CheckResponse checkResponse = new CheckResponse();
        checkResponse.setResult(false);

        return checkResponse;
    }
}
