package main.services;

import main.Util.PostToDTOCustomMapper;
import main.config.Config;
import main.config.exception.NotFoundException;
import main.model.DTO.PostDTO;
import main.model.Post;
import main.model.enums.OutputMode;
import main.repository.PostCommentRepository;
import main.repository.PostRepository;
import main.repository.PostVoteRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Map<Long, List<PostDTO>> searchPosts(Integer offset, Integer limit, String searchText) {

        LocalDateTime dateTime = LocalDateTime.now();

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.searchPostsByText(searchText, dateTime, pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = new ArrayList<>();
        //используем кастомный маппер
        postsPageable.forEach(post -> postDTOs.add(postToDTOCustomMapper.postToDTOCustomMapper(post)));

        //Замапим нужные значения в ДТОшки
        postDTOs.forEach(postDTO -> postDTO.setCommentCount(postCommentRepository.countCommentsByPost(postDTO.getId())));
        postDTOs.forEach(postDTO -> postDTO.setDislikeCount(postVoteRepository.countDisLikesByPost(postDTO.getId())));
        postDTOs.forEach(postDTO -> postDTO.setLikeCount(postVoteRepository.countLikesByPost(postDTO.getId())));

        Map<Long, List<PostDTO>> map = new HashMap<>();
        map.put(countAllPosts, postDTOs);
        return map;
    }

    public Map<Long, List<PostDTO>> getPosts(Integer offset,
                                             Integer limit,
                                             String mode) throws NotFoundException {

        LocalDateTime dateTime = LocalDateTime.now();

        Pageable pageable = null;
        Page<Post> postsPageable = null;

        if (mode.equalsIgnoreCase(OutputMode.RECENT.toString())) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
            postsPageable = postRepository.getAllPostsTimeSort(dateTime, pageable);
        } else if (mode.equalsIgnoreCase(OutputMode.EARLY.toString())) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
            postsPageable = postRepository.getAllPostsTimeSort(dateTime, pageable);
        } else if (mode.equalsIgnoreCase(OutputMode.BEST.toString())) {
            pageable = PageRequest.of(offset / limit, limit);
            postsPageable = postRepository.getAllPostsLikesSort(dateTime, pageable);
        } else if (mode.equalsIgnoreCase(OutputMode.POPULAR.toString())) {
            pageable = PageRequest.of(offset / limit, limit);
            postsPageable = postRepository.getAllPostsCommentSort(dateTime, pageable);
        }

        List<PostDTO> postDTOs = new ArrayList<>();

        if (postsPageable == null) {
            throw new NotFoundException(Config.ERROR_NO_POSTS_IN_DB);
        }

        //используем кастомный маппер
        postsPageable.forEach(post -> postDTOs.add(postToDTOCustomMapper.postToDTOCustomMapper(post)));

        //Замапим нужные значения в ДТОшки
        postDTOs.forEach(postDTO -> postDTO.setCommentCount(postCommentRepository.countCommentsByPost(postDTO.getId())));
        postDTOs.forEach(postDTO -> postDTO.setDislikeCount(postVoteRepository.countDisLikesByPost(postDTO.getId())));
        postDTOs.forEach(postDTO -> postDTO.setLikeCount(postVoteRepository.countLikesByPost(postDTO.getId())));

        Map<Long, List<PostDTO>> map = new HashMap<>();
        map.put(postsPageable.getTotalElements(), postDTOs);
        return map;
    }

    public ResponseEntity<?> getPostByID(Integer postID) {
        Post post = postRepository.getPostByID(postID);
        
        return ResponseEntity.ok(null);
    }
}
