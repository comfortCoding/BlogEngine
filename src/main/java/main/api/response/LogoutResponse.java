package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LogoutResponse {

    @JsonProperty(value = "result")
    private boolean result;

}
