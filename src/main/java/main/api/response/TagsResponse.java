package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import main.model.dto.TagDTO;

import java.util.List;

@Data
public class TagsResponse {

    @JsonProperty(value = "tags")
    private List<TagDTO> tags;
}
