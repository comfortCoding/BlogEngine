package main.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "timestamp")
    private Long timestamp;

    @JsonProperty(value = "user")
    private UserDTO user;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "announce")
    private String text;

    @JsonProperty(value = "likeCount")
    private Integer likeCount;

    @JsonProperty(value = "dislikeCount")
    private Integer dislikeCount;

    @JsonProperty(value = "commentCount")
    private Integer commentCount;

    @JsonProperty(value = "viewCount")
    private Integer viewCount;



}

