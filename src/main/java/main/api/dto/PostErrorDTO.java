package main.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostErrorDTO {

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "text")
    private String text;

}
