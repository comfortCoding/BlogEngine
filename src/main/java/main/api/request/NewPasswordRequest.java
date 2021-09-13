package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewPasswordRequest {

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "captcha")
    private String captcha;

    @JsonProperty(value = "captcha_secret")
    private String captchaSecret;

}
