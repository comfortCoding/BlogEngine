package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.dto.UserDTO;

@Data
public class LoginResponse {

    @JsonProperty(value = "result")
    private boolean result;

    @JsonProperty(value = "user")
    private UserDTO user;
}
