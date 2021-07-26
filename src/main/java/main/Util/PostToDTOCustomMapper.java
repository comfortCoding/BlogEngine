package main.Util;

import main.model.dto.PostDTO;
import main.model.Post;
import org.mapstruct.Mapper;

@Mapper(uses = { DateToSecondMapper.class, TextToAnnounceMapper.class })
public interface PostToDTOCustomMapper {

    default PostDTO postToDTOCustomMapper(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setActive(post.isActive());
        postDTO.setTimestamp(new DateToSecondMapper().dateToSecond(post.getTime()));
        postDTO.setUser(new UserToDTOMapper().convertToDto(post.getUser()));
        postDTO.setTitle(post.getTitle());
        postDTO.setText(new TextToAnnounceMapper().textToAnnounce(post.getText()));
        postDTO.setViewCount(post.getViewCount());

        postDTO.setCommentsList(post.getPostCommentList());
        postDTO.setCommentCount(post.getPostCommentList().size());
        postDTO.setLikeCount(post.getPostLikeList().size());
        postDTO.setDislikeCount(post.getPostDisLikeList().size());

        return postDTO;
    }
}
