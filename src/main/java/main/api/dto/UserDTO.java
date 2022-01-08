package main.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "photo")
    private String photo;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "moderation")
    private boolean moderation;

    @JsonProperty(value = "moderationCounter")
    private Integer moderationCounter;

    @JsonProperty(value = "settings")
    private boolean settings;

}
