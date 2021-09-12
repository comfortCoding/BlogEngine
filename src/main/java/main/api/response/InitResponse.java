package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.config.Config;

@Data
public class InitResponse {

    @JsonProperty(value = "title")
    private String title = Config.TITLE;

    @JsonProperty(value = "subtitle")
    private String subtitle = Config.SUBTITLE;

    @JsonProperty(value = "phone")
    private String phone = Config.PHONE;

    @JsonProperty(value = "email")
    private String email = Config.EMAIL;

    @JsonProperty(value = "copyright")
    private String copyright = Config.COPYRIGHT;

    @JsonProperty(value = "copyrightFrom")
    private String copyrightFrom = Config.COPYRIGHT_FROM;

}
