package main.Util;

import main.model.DTO.TagDTO;
import main.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TagToDTOMapper {
    private final ModelMapper modelMapper;

    public TagToDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Tag.class, TagDTO.class);
    }

    public TagDTO convertToDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }
}
