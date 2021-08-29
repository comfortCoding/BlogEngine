package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CaptchaResponse {

    @JsonProperty(value = "secret")
    private String secret;

    @JsonProperty(value = "image")
    private String image;
}
