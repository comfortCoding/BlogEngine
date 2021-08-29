package main.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "parent_id")
    private Integer parentID;

    @JsonProperty(value = "post_id")
    private Integer postID;

    @JsonProperty(value = "user_id")
    private UserDTO user;

    @JsonProperty(value = "time")
    private Date time;

    @JsonProperty(value = "text")
    private String text;
}
