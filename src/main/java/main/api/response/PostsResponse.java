package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import main.api.dto.PostDTO;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostsResponse {

    @JsonProperty(value = "count")
    private Long count;

    @JsonProperty(value = "posts")
    private List<PostDTO> posts;

    public static PostsResponse create() {
        return new PostsResponse();
    }
}
