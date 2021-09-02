package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModerateRequest {

    @JsonProperty(value = "post_id")
    private Integer postID;

    @JsonProperty(value = "decision")
    private String decision;

}
