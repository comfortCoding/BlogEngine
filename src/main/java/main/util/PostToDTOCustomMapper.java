package main.util;

import main.model.dto.PostDTO;
import main.model.Post;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Mapper(uses = {DateToSecondMapper.class, TextToAnnounceMapper.class, CommentToDTOMapper.class})
public interface PostToDTOCustomMapper {

    default PostDTO postToDTOCustomMapper(Post post) {

        if (post == null) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setActive(post.isActive());
        postDTO.setTimestamp(new DateToSecondMapper().dateToSecond(post.getTime()));
        postDTO.setUser(new UserToDTOMapper().convertToDto(post.getUser()));
        postDTO.setTitle(post.getTitle());
        postDTO.setAnnounce(new TextToAnnounceMapper().textToAnnounce(post.getText()));
        postDTO.setText(post.getText());
        postDTO.setViewCount(post.getViewCount());

        postDTO.setCommentsList(new CommentToDTOMapper().convertToDTO(post.getPostCommentList()));
        postDTO.setCommentCount(post.getPostCommentList().size());
        postDTO.setLikeCount(post.getPostLikeList().size());
        postDTO.setDislikeCount(post.getPostDisLikeList().size());

        return postDTO;
    }

    default List<PostDTO> mapper(Page<Post> posts) {
        List<PostDTO> postDTOs = new ArrayList<>();
        posts.forEach(post -> postDTOs.add(postToDTOCustomMapper(post)));
        return postDTOs;
    }
}
