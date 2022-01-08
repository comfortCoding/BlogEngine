package main.util;

import main.api.dto.TagDTO;
import main.model.answer.TagAnswer;
import org.mapstruct.Mapper;

@Mapper
public interface TagToDTOCustomMapper {

    default TagDTO tagToDTOCustomMapper(TagAnswer tagAnswer) {
        if (tagAnswer == null) {
            return null;
        }

        TagDTO tagDTO = new TagDTO();

        tagDTO.setName(tagAnswer.getName());
        tagDTO.setWeight(tagAnswer.getWeight());

        return tagDTO;
    }
}
