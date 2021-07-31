package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RegisterRequest {

    @JsonProperty(value = "e_mail")
    private String email;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "captcha")
    private String captcha;

    @JsonProperty(value = "captcha_secret")
    private String captchaSecret;

}
