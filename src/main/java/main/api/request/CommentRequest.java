package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {

    @JsonProperty(value = "parent_id")
    private Integer parentID;

    @JsonProperty(value = "post_id")
    private Integer postID;

    @JsonProperty(value = "text")
    private String text;
}
