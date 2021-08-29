package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InitResponse {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "subtitle")
    private String subtitle;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "copyright")
    private String copyright;

    @JsonProperty(value = "copyrightFrom")
    private String copyrightFrom;

}
