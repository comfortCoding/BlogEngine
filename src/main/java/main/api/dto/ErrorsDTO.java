package main.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorsDTO {

    @JsonProperty(value = "email")
    private String emailError;

    @JsonProperty(value = "name")
    private String nameError;

    @JsonProperty(value = "password")
    private String passwordError;

    @JsonProperty(value = "captcha")
    private String captchaError;

    @JsonProperty(value = "code")
    private String codeError;

}
