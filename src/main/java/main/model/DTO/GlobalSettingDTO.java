package main.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalSettingDTO {

    @JsonProperty(value = "id")
    private int id;

    @JsonProperty(value = "code")
    private String code;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "value")
    private String value;

    public Boolean getBoolValue(){
        return this.value.equalsIgnoreCase("YES");
    }
}
