package main.api.response;

import lombok.Data;
import main.model.DTO.TagDTO;

import java.util.List;

@Data
public class TagsResponse {

    private List<TagDTO> tags;
}
