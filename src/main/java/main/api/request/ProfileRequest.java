package main.api.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Base64;

@Data
public class ProfileRequest {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "removePhoto")
    private Integer removePhoto = 0;

    @JsonProperty(value = "photo")
    private Base64 photo;

}
