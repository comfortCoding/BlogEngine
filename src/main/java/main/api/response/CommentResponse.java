package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.dto.CommentErrorDTO;

import java.util.List;

@Data
public class CommentResponse {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "result")
    private boolean result;

    @JsonProperty(value = "errors")
    private List<CommentErrorDTO> errors;
}
