package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.dto.PostDTO;

import java.util.List;

@Data
public class PostsResponse {

    @JsonProperty(value = "count")
    private Long count;

    @JsonProperty(value = "posts")
    private List<PostDTO> posts;
}
