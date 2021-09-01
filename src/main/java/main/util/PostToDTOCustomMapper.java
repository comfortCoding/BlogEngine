package main.util;

import main.model.dto.PostDTO;
import main.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {DateToSecondMapper.class, TextToAnnounceMapper.class, CommentToDTOMapper.class})
public interface PostToDTOCustomMapper {

    default PostDTO convertToDTO(Post post) {

        if (post == null) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setActive(post.isActive());
        postDTO.setTimestamp(new DateToSecondMapper().dateToSecond(post.getTime()));
        postDTO.setUser(Mappers.getMapper(UserToDTOCustomMapper.class).convertToDTO(post.getUser()));
        postDTO.setTitle(post.getTitle());
        postDTO.setAnnounce(new TextToAnnounceMapper().textToAnnounce(post.getText()));
        postDTO.setText(post.getText());
        postDTO.setViewCount(post.getViewCount());

        postDTO.setCommentsList(Mappers.getMapper(CommentToDTOMapper.class).convertToDTO(post.getPostCommentList()));
        postDTO.setCommentCount(post.getPostCommentList().size());
        postDTO.setLikeCount(post.getPostLikeList().size());
        postDTO.setDislikeCount(post.getPostDisLikeList().size());

        return postDTO;
    }

    default List<PostDTO> convertToDTO(Page<Post> posts) {
        List<PostDTO> postDTOs = new ArrayList<>();
        posts.forEach(post -> postDTOs.add(convertToDTO(post)));
        return postDTOs;
    }
}
