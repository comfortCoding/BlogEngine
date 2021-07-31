package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.dto.RegistrationErrorsDTO;

@Data
public class RegisterResponse {

    @JsonProperty(value = "result")
    private boolean result;

    @JsonProperty(value = "errors")
    private RegistrationErrorsDTO errors;
}
