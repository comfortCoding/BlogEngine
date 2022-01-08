package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.api.dto.PostErrorDTO;

import java.util.List;

@Data
public class PostDataResponse {

    @JsonProperty(value = "result")
    private boolean result;

    @JsonProperty(value = "errors")
    private List<PostErrorDTO> errors;

}
