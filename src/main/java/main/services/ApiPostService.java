package main.services;

import main.api.request.PostDataRequest;
import main.api.response.PostDataResponse;
import main.model.Tag;
import main.model.User;
import main.model.dto.CreatePostErrorDTO;
import main.model.enums.ModerationStatus;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static main.config.Config.*;

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

    public PostDataResponse addPost(PostDataRequest request) {

        PostDataResponse correctPostData = isCorrectPostData(request);

        if (!correctPostData.isResult()) return correctPostData;

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        postDataRepository(new Post(), request, currentUserEmail);

        PostDataResponse response = new PostDataResponse();
        response.setResult(true);
        return response;
    }

    public PostDataResponse updatePost(Integer postID, PostDataRequest request) {
        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        
        Post post = postRepository.getPostByID(postID);
        
        postDataRepository(post, request, currentUserEmail);

        PostDataResponse response = new PostDataResponse();
        response.setResult(true);
        return response;
    }

    public PostsResponse getMyPosts(Integer offset,
                                    Integer limit,
                                    String postStatus) {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        if (currentUser == null) {
            return new PostsResponse();
        }

        Integer currentUserID = currentUser.getId();

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

    private List<Tag> createTagsIfNew(List<String> tagsRequest) {
        List<Tag> tags = new ArrayList<>();
        if (tagsRequest != null) {

            for (String tagName : tagsRequest) {

                Tag tag = tagRepository.findTagByName(tagName);

                if (tag == null) {
                    Tag newTag = new Tag();
                    newTag.setName(tagName);
                    tags.add(tagRepository.save(newTag));
                } else {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    private void postDataRepository(Post post, PostDataRequest request, String currentUserEmail) {
        post.setTime(Instant.ofEpochMilli(request.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime()
                .compareTo(LocalDateTime.now()) < 0
                ? LocalDateTime.now()
                : Instant.ofEpochMilli(request.getTimestamp()).atZone(ZoneId.systemDefault()).toLocalDateTime());
        post.setTitle(request.getTitle());
        post.setText(request.getText());
        post.setActive(request.getActive() == 1);
        post.setPostTagList(createTagsIfNew(request.getTags()));
        post.setModerationStatus(ModerationStatus.NEW);
        post.setUser(userRepository.findUserByEmail(currentUserEmail));
        postRepository.save(post);
    }

    private PostDataResponse isCorrectPostData(PostDataRequest request) {

        PostDataResponse response = new PostDataResponse();

        if (request.getTitle().length() < MIN_POST_TITLE_LENGTH) {

            CreatePostErrorDTO titleError = new CreatePostErrorDTO();
            titleError.setTitleError(POST_TITLE_SHORT_ERROR);
            List<CreatePostErrorDTO> errorDTOS = new ArrayList<>();
            errorDTOS.add(titleError);

            response.setResult(false);
            response.setErrors(errorDTOS);
            return response;
        }

        if (request.getText().length() < MIN_POST_BODY_LENGTH) {

            CreatePostErrorDTO titleError = new CreatePostErrorDTO();
            titleError.setTitleError(POST_BODY_SHORT_ERROR);
            List<CreatePostErrorDTO> errorDTOS = new ArrayList<>();
            errorDTOS.add(titleError);

            response.setResult(false);
            response.setErrors(errorDTOS);

            return response;
        }

        response.setResult(true);

        return response;
    }

}
