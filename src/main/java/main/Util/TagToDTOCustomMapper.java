package main.Util;

import main.model.dto.TagDTO;
import main.model.answer.TagAnswer;
import org.mapstruct.Mapper;

@Mapper
public interface TagToDTOCustomMapper {

    default TagDTO tagToDTOCustomMapper(TagAnswer tagAnswer) {
        TagDTO tagDTO = new TagDTO();

        tagDTO.setName(tagAnswer.getName());
        tagDTO.setWeight(tagAnswer.getWeight());

        return tagDTO;
    }
}
