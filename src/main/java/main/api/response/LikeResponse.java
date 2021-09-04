package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LikeResponse {

    @JsonProperty(value = "result")
    private boolean result;

}
