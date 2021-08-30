package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDataRequest {

    @JsonProperty(value = "timestamp")
    private Long timestamp;

    @JsonProperty(value = "active")
    private Integer active;

    @JsonProperty(value = "title")
    private String title;

    @JsonProperty(value = "tags")
    private List<String> tags;

    @JsonProperty(value = "text")
    private String text;
}
