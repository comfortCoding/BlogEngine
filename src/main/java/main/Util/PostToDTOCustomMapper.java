package main.Util;

import main.model.DTO.PostDTO;
import main.model.Post;
import org.mapstruct.Mapper;

@Mapper(uses = { DateToSecondMapper.class, TextToAnnounceMapper.class })
public interface PostToDTOCustomMapper {



    default PostDTO postToDTOCustomMapper(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.setId(post.getId());
        postDTO.setTimestamp(new DateToSecondMapper().dateToSecond(post.getTime()));
        postDTO.setUser(new UserToDTOMapper().convertToDto(post.getUser()));
        postDTO.setTitle(post.getTitle());
        postDTO.setText(new TextToAnnounceMapper().textToAnnounce(post.getText()));
        //postDTO.setLikeCount(0);
        //postDTO.setDislikeCount(0);
        //postDTO.setCommentCount(0);
        postDTO.setViewCount(post.getViewCount());

        return postDTO;
    }


}