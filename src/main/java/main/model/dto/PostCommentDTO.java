package main.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "parent_id")
    private Integer parentID;

    @JsonProperty(value = "post_id")
    private Integer postID;

    @JsonProperty(value = "user")
    private UserDTO user;

    @JsonProperty(value = "timestamp")
    private Long time;

    @JsonProperty(value = "text")
    private String text;
}
