package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import main.config.Config;

@Data
@Accessors(chain = true)
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

    public static InitResponse create(){
        return new InitResponse()
                .setTitle(Config.TITLE)
                .setSubtitle(Config.SUBTITLE)
                .setPhone(Config.PHONE)
                .setEmail(Config.EMAIL)
                .setCopyright(Config.COPYRIGHT)
                .setCopyrightFrom(Config.COPYRIGHT_FROM);
    }
}
