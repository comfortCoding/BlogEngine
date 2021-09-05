package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatisticsResponse {
    @JsonProperty(value = "postsCount")
    private Integer postsCount;

    @JsonProperty(value = "likesCount")
    private Integer likesCount;

    @JsonProperty(value = "dislikesCount")
    private Integer dislikesCount;

    @JsonProperty(value = "viewsCount")
    private Integer viewsCount;

    @JsonProperty(value = "firstPublication")
    private Long firstPublication;
}
