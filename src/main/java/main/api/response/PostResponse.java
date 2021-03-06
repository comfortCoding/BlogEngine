package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import main.api.dto.PostCommentDTO;
import main.api.dto.UserDTO;

import java.util.List;

@Data
@Accessors(chain = true)
public class PostResponse {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "timestamp")
    private Long timestamp;

    @JsonProperty(value = "active")
    private boolean isActive;

    @JsonProperty(value = "user")
    private UserDTO user;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "text")
    private String text;

    @JsonProperty(value = "likeCount")
    private Integer likeCount;

    @JsonProperty(value = "dislikeCount")
    private Integer dislikeCount;

    @JsonProperty(value = "commentCount")
    private Integer commentCount;

    @JsonProperty(value = "viewCount")
    private Integer viewCount;

    @JsonProperty(value = "comments")
    private List<PostCommentDTO> comments;

    @JsonProperty(value = "tags")
    private List<String> tags;

    public static PostResponse create(){
        return new PostResponse();
    }
}
