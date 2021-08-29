package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.dto.UserDTO;

@Data
public class LoginResponse {

    @JsonProperty(value = "result")
    private boolean result;

    @JsonProperty(value = "user")
    private UserDTO user;
}
