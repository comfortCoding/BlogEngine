package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import main.api.dto.TagDTO;

import java.util.List;

@Data
@Accessors(chain = true)
public class TagsResponse {

    @JsonProperty(value = "tags")
    private List<TagDTO> tags;

    public static TagsResponse create() {
        return new TagsResponse();
    }
}
