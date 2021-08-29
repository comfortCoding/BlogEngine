package main.services;

import main.model.User;
import main.model.enums.PostStatus;
import main.repository.UserRepository;
import main.util.PostToDTOCustomMapper;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApiPostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;

    private final PostToDTOCustomMapper postToDTOCustomMapper;

    public ApiPostService(UserRepository userRepository,
                          PostRepository postRepository,
                          TagRepository tagRepository) {
        this.userRepository = userRepository;
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

        List<PostDTO> postDTOs = postToDTOCustomMapper.mapper(postsPageable);

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

        List<PostDTO> postDTOs = postToDTOCustomMapper.mapper(postsPageable);

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

        List<PostDTO> postDTOs = postToDTOCustomMapper.mapper(postsPageable);

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

        Long countAllPosts = Objects.requireNonNull(postsPageable).getTotalElements();

        List<PostDTO> postDTOs = postToDTOCustomMapper.mapper(postsPageable);

        PostsResponse response = new PostsResponse();
        response.setCount(countAllPosts);
        response.setPosts(postDTOs);

        return response;
    }

    public PostsResponse getMyPosts(Integer offset,
                      Integer limit,
                      String postStatus) {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Integer currentUserID = userRepository.findUserByEmail(currentUserEmail).getId();

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
        Page<Post> postsPageable = null;

        if (postStatus.equalsIgnoreCase(PostStatus.INACTIVE.toString())) {
            postsPageable = postRepository.getInactivePosts(currentUserID, pageable);
        } else if (postStatus.equalsIgnoreCase(PostStatus.PENDING.toString())) {
            postsPageable = postRepository.getPendingPosts(currentUserID, pageable);
        } else if (postStatus.equalsIgnoreCase(PostStatus.DECLINED.toString())) {
            postsPageable = postRepository.getDeclinedPosts(currentUserID, pageable);
        } else if (postStatus.equalsIgnoreCase(PostStatus.PUBLISHED.toString())) {
            postsPageable = postRepository.getPublishedPosts(currentUserID, pageable);
        }

        Long countAllPosts = Objects.requireNonNull(postsPageable).getTotalElements();
        List<PostDTO> postDTOs = postToDTOCustomMapper.mapper(postsPageable);

        PostsResponse response = new PostsResponse();
        response.setCount(countAllPosts);
        response.setPosts(postDTOs);

        return response;
    }

    public PostResponse getPostByID(Integer postID) {

        Post post = postRepository.getPostByID(postID);

        PostDTO postDTO = postToDTOCustomMapper.postToDTOCustomMapper(post);

        List<String> tags = tagRepository.getTagNamesByPostID(postID);

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

}
