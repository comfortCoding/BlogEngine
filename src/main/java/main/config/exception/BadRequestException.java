package main.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends AppException {

    public BadRequestException(String errorDescription) {
        super("INVALID_REQUEST", errorDescription);
    }
}
