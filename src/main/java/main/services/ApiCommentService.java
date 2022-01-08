package main.services;

import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.model.Post;
import main.model.PostComment;
import main.model.User;
import main.api.dto.CommentErrorDTO;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.UserRepository;
import main.util.CommentToDTOMapper;
import org.mapstruct.factory.Mappers;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static main.config.Config.*;

@Service
public class ApiCommentService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;

    private final CommentToDTOMapper commentToDTOMapper;

    public ApiCommentService(UserRepository userRepository,
                             PostRepository postRepository,
                             PostCommentRepository postCommentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.commentToDTOMapper = Mappers.getMapper(CommentToDTOMapper.class);
    }

    public CommentResponse addComment(CommentRequest request){

        CommentResponse response = isCorrectCommentData(request.getText());
        if (!response.isResult()) return response;

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        Post post = postRepository.getPostByID(request.getPostID());

        PostComment comment = new PostComment();

        comment.setTime(LocalDateTime.now());
        comment.setPost(post);
        if (request.getParentID() != null) {
            PostComment parentComment = postCommentRepository.getCommentByID(request.getParentID());
            comment.setParentComment(parentComment);
        } else {
            comment.setParentComment(null);
        }
        comment.setText(request.getText());
        comment.setUser(currentUser);

        Integer commentID = postCommentRepository.save(comment).getId();


        response.setId(commentID);

        return response;
    }

    private CommentResponse isCorrectCommentData(String textForCheck) {

        CommentResponse response = new CommentResponse();

        if (textForCheck.length() < MIN_COMMENT_BODY_LENGTH) {

            CommentErrorDTO textError = new CommentErrorDTO();
            textError.setText(COMMENT_BODY_SHORT_ERROR);

            List<CommentErrorDTO> errorDTOS = new ArrayList<>();
            errorDTOS.add(textError);

            response.setResult(false);
            response.setErrors(errorDTOS);

            return response;
        }

        response.setResult(true);

        return response;
    }
}
