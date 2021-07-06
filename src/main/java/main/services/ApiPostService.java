package main.services;

import main.Util.PostToDTOCustomMapper;
import main.api.response.PostsResponse;
import main.model.DTO.PostDTO;
import main.model.Post;
import main.model.enums.OutputMode;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ApiPostService {
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final PostVoteRepository postVoteRepository;

    private final PostToDTOCustomMapper postToDTOCustomMapper;

    public ApiPostService(PostRepository postRepository,
                          PostCommentRepository postCommentRepository,
                          PostVoteRepository postVoteRepository
                          ) {
        this.postRepository = postRepository;
        this.postCommentRepository = postCommentRepository;
        this.postVoteRepository = postVoteRepository;

        this.postToDTOCustomMapper = Mappers.getMapper(PostToDTOCustomMapper.class);
    }

    public ResponseEntity<PostsResponse> getPosts(Integer offset, Integer limit, String mode) {

        List<Post> allPosts = postRepository.getAllPosts(LocalDateTime.now());
        Integer postCount = allPosts.size();

        List<Post> responsePosts = new ArrayList<>(limit);

        if (allPosts.size() > offset + limit) {
            responsePosts = allPosts.subList(offset, offset + limit);
        } else {
            responsePosts = allPosts.subList(offset, allPosts.size());
        }

        List<PostDTO> postDTOs = new ArrayList<>();
        //используем кастомный маппер
        responsePosts.forEach(post -> postDTOs.add(postToDTOCustomMapper.postToDTOCustomMapper(post)));

        //Замапим нужные значения в ДТОшки
        postDTOs.forEach(postDTO -> postDTO.setCommentCount(postCommentRepository.countCommentsByPost(postDTO.getId())));
        postDTOs.forEach(postDTO -> postDTO.setDislikeCount(postVoteRepository.countDisLikesByPost(postDTO.getId())));
        postDTOs.forEach(postDTO -> postDTO.setLikeCount(postVoteRepository.countLikesByPost(postDTO.getId())));

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();
        response.setCount(postCount);
        response.setPosts(sortPostDTOByMode(postDTOs, mode));

        return ResponseEntity
                .ok(response);
    }

    /** Метод для сортировки postDTOs, метод сортировки выбирается по значению mode
     */
    private List<PostDTO> sortPostDTOByMode(List<PostDTO> postDTOs, String mode) {

        if (mode.equalsIgnoreCase(OutputMode.RECENT.toString())) {

            //recent - сортировать по дате публикации, сначала новые
            postDTOs.sort((post1, post2) -> post2.getTimestamp().compareTo(post1.getTimestamp()));

        } else if (mode.equalsIgnoreCase(OutputMode.POPULAR.toString())) {

            //popular - сортировать по убыванию количества комментариев
            postDTOs.sort((post1, post2) -> post2.getCommentCount().compareTo(post1.getCommentCount()));
        } else if (mode.equalsIgnoreCase(OutputMode.BEST.toString())) {

            //best - сортировать по убыванию количества лайков
            postDTOs.sort((post1, post2) -> post2.getLikeCount().compareTo(post1.getLikeCount()));

        } else if (mode.equalsIgnoreCase(OutputMode.EARLY.toString())) {

            //early - сортировать по дате публикации, сначала старые
            postDTOs.sort((post1, post2) -> post1.getTimestamp().compareTo(post2.getTimestamp()));
        }

        return postDTOs;
    }

}
