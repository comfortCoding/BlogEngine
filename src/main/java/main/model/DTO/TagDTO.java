package main.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDTO {

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "weight")
    private Double weight;
}
