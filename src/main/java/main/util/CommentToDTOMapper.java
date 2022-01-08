package main.util;

import main.model.PostComment;
import main.api.dto.PostCommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {DateToSecondConverter.class})
public interface CommentToDTOMapper {

    default PostCommentDTO convertToDTO(PostComment comment) {
        PostCommentDTO commentDTO = new PostCommentDTO();

        commentDTO.setParentID(comment.getParentComment() == null ? null : comment.getParentComment().getId());
        commentDTO.setText(comment.getText());
        commentDTO.setId(comment.getId());
        commentDTO.setPostID(comment.getPost().getId());
        commentDTO.setTime(new DateToSecondConverter().dateToSecond(comment.getTime()));
        commentDTO.setUser(Mappers.getMapper(UserToDTOCustomMapper.class).convertToDTO(comment.getUser()));

        return commentDTO;
    }

    default List<PostCommentDTO> convertToDTO(List<PostComment> comments) {
        List<PostCommentDTO> commentDTOs = new ArrayList<>();
        comments.forEach(comment -> commentDTOs.add(convertToDTO(comment)));
        return commentDTOs;
    }
}
