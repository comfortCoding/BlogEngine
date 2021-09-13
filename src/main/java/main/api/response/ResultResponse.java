package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import main.model.dto.ErrorsDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {

    @JsonProperty(value = "result")
    private boolean result;

    @JsonProperty(value = "errors")
    private ErrorsDTO errors;
}
