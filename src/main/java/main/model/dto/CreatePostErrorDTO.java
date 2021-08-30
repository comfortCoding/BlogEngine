package main.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostErrorDTO {

    @JsonProperty(value = "title")
    private String titleError;

    @JsonProperty(value = "text")
    private String textError;

}
