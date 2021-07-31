package main.services;

import main.Util.PostToDTOCustomMapper;
import main.api.response.PostResponse;
import main.api.response.PostsResponse;
import main.config.exception.NotFoundException;
import main.model.dto.PostDTO;
import main.model.Post;
import main.model.enums.OutputMode;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApiPostService {
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    private final PostToDTOCustomMapper postToDTOCustomMapper;

    public ApiPostService(PostRepository postRepository,
                          TagRepository tagRepository) {
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;

        this.postToDTOCustomMapper = Mappers.getMapper(PostToDTOCustomMapper.class);
    }

    public PostsResponse searchPosts(Integer offset,
                                     Integer limit,
                                     String searchText) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.searchPostsByText(searchText, LocalDateTime.now(), pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = mapPosts(postsPageable);

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();
        response.setCount(countAllPosts);
        response.setPosts(postDTOs);

        return response;
    }

    public PostsResponse searchByDate(Integer offset,
                                      Integer limit,
                                      String searchDateString) {

        LocalDateTime dateTimeFrom = LocalDateTime.parse(searchDateString + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateTimeTo = LocalDateTime.parse(searchDateString + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.getAllPostsByDate(dateTimeFrom, dateTimeTo, pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = mapPosts(postsPageable);

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();

        response.setCount(countAllPosts);
        response.setPosts(postDTOs);

        return response;
    }

    public PostsResponse searchByTag(Integer offset,
                                     Integer limit,
                                     String tag) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.getAllPostsByTag(tag, LocalDateTime.now(), pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = mapPosts(postsPageable);

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();

        response.setCount(countAllPosts);
        response.setPosts(postDTOs);

        return response;
    }

    public PostsResponse getPosts(Integer offset,
                                  Integer limit,
                                  String mode) throws NotFoundException {

        Pageable pageable = null;
        Page<Post> postsPageable = null;

        if (mode.equalsIgnoreCase(OutputMode.RECENT.toString())) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
            postsPageable = postRepository.getAllPostsTimeSort(LocalDateTime.now(), pageable);
        } else if (mode.equalsIgnoreCase(OutputMode.EARLY.toString())) {
            pageable = PageRequest.of(offset / limit, limit, Sort.by("time").ascending());
            postsPageable = postRepository.getAllPostsTimeSort(LocalDateTime.now(), pageable);
        } else if (mode.equalsIgnoreCase(OutputMode.BEST.toString())) {
            pageable = PageRequest.of(offset / limit, limit);
            postsPageable = postRepository.getAllPostsLikesSort(LocalDateTime.now(), pageable);
        } else if (mode.equalsIgnoreCase(OutputMode.POPULAR.toString())) {
            pageable = PageRequest.of(offset / limit, limit);
            postsPageable = postRepository.getAllPostsCommentSort(LocalDateTime.now(), pageable);
        }

        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = mapPosts(postsPageable);

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();
        response.setCount(countAllPosts);
        response.setPosts(postDTOs);

        return response;
    }

    public PostResponse getPostByID(Integer postID) {

        Post post = postRepository.getPostByID(postID);

        PostDTO postDTO = postToDTOCustomMapper.postToDTOCustomMapper(post);

        List<String> tags = tagRepository.getTagNamesByPostID(postID);

        //Сформируем ответ для фронта
        PostResponse response = new PostResponse();

        response.setId(postDTO.getId());
        response.setTimestamp(postDTO.getTimestamp());
        response.setActive(postDTO.isActive());
        response.setUser(postDTO.getUser());
        response.setTitle(postDTO.getTitle());
        response.setText(postDTO.getText());
        response.setLikeCount(postDTO.getLikeCount());
        response.setDislikeCount(postDTO.getDislikeCount());
        response.setViewCount(postDTO.getViewCount());
        response.setComments(postDTO.getCommentsList());
        response.setTags(tags);

        return response;
    }

    /**
     * Внутренние методы сервиса
     */

    private List<PostDTO> mapPosts(Page<Post> postsPageable) {
        List<PostDTO> postDTOs = new ArrayList<>();

        //используем кастомный маппер
        postsPageable.forEach(post -> postDTOs.add(postToDTOCustomMapper.postToDTOCustomMapper(post)));

        return postDTOs;
    }
}
