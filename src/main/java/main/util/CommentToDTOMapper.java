package main.util;

import main.model.PostComment;
import main.model.dto.PostCommentDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommentToDTOMapper {

    private final ModelMapper modelMapper;

    public CommentToDTOMapper() {
        this.modelMapper = new ModelMapper();

        modelMapper.createTypeMap(PostComment.class, PostCommentDTO.class);
    }

    public PostCommentDTO convertToDTO(PostComment comment) {
        return modelMapper.map(comment, PostCommentDTO.class);
    }

    public List<PostCommentDTO> convertToDTO(List<PostComment> comments) {
        List<PostCommentDTO> commentDTOs = new ArrayList<>();
        comments.forEach(comment -> modelMapper.map(comment, PostCommentDTO.class));
        return commentDTOs;
    }
}
