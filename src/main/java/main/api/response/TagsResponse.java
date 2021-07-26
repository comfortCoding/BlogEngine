package main.api.response;

import lombok.Data;
import main.model.dto.TagDTO;

import java.util.List;

@Data
public class TagsResponse {

    private List<TagDTO> tags;
}
