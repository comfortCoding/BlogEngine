package main.api.response;

import lombok.Data;
import main.model.DTO.PostDTO;

import java.util.List;

@Data
public class PostsResponse {

    private Long count;

    private List<PostDTO> posts;
}
