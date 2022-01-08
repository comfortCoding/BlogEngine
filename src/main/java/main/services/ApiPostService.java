package main.services;

import main.api.request.LikeRequest;
import main.api.request.ModerateRequest;
import main.api.request.PostDataRequest;
import main.api.response.*;
import main.model.PostVote;
import main.model.Tag;
import main.model.User;
import main.api.dto.PostErrorDTO;
import main.model.enums.ModeratePostStatus;
import main.model.enums.ModerationStatus;
import main.model.enums.PostStatus;
import main.repository.PostVoteRepository;
import main.repository.UserRepository;
import main.util.PostToDTOCustomMapper;
import main.config.exception.NotFoundException;
import main.api.dto.PostDTO;
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
    private final PostVoteRepository postVoteRepository;

    private final PostToDTOCustomMapper postToDTOCustomMapper;

    public ApiPostService(UserRepository userRepository,
                          PostRepository postRepository,
                          TagRepository tagRepository,
                          PostVoteRepository postVoteRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.postVoteRepository = postVoteRepository;

        this.postToDTOCustomMapper = Mappers.getMapper(PostToDTOCustomMapper.class);
    }

    public PostsResponse searchPosts(Integer offset,
                                     Integer limit,
                                     String searchText) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.searchPostsByText(searchText, LocalDateTime.now(), pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = postToDTOCustomMapper.convertToDTO(postsPageable);

        return PostsResponse.create()
                .setCount(countAllPosts)
                .setPosts(postDTOs);
    }

    public PostsResponse searchByDate(Integer offset,
                                      Integer limit,
                                      String searchDateString) {

        LocalDateTime dateTimeFrom = LocalDateTime.parse(searchDateString + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime dateTimeTo = LocalDateTime.parse(searchDateString + " 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.getAllPostsByDate(dateTimeFrom, dateTimeTo, pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = postToDTOCustomMapper.convertToDTO(postsPageable);

        return PostsResponse.create()
                .setCount(countAllPosts)
                .setPosts(postDTOs);
    }

    public PostsResponse searchByTag(Integer offset,
                                     Integer limit,
                                     String tag) {

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());

        Page<Post> postsPageable = postRepository.getAllPostsByTag(tag, LocalDateTime.now(), pageable);
        Long countAllPosts = postsPageable.getTotalElements();

        List<PostDTO> postDTOs = postToDTOCustomMapper.convertToDTO(postsPageable);

        return PostsResponse.create()
                .setCount(countAllPosts)
                .setPosts(postDTOs);
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

        List<PostDTO> postDTOs = postToDTOCustomMapper.convertToDTO(postsPageable);

        return PostsResponse.create()
                .setCount(countAllPosts)
                .setPosts(postDTOs);
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

    public PostsResponse getPostsByPerson(Integer offset,
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
        List<PostDTO> postDTOs = postToDTOCustomMapper.convertToDTO(postsPageable);

        return PostsResponse.create()
                .setCount(countAllPosts)
                .setPosts(postDTOs);
    }

    public PostsResponse getPostsForModeration(Integer offset,
                                               Integer limit,
                                               String moderationStatus) {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Integer currentUserID = userRepository.findUserByEmail(currentUserEmail).getId();

        Pageable pageable = PageRequest.of(offset / limit, limit, Sort.by("time").descending());
        Page<Post> postsPageable = null;

        if (moderationStatus.equalsIgnoreCase(ModerationStatus.NEW.toString())) {
            postsPageable = postRepository.getNewPosts(pageable);
        } else if (moderationStatus.equalsIgnoreCase(ModerationStatus.ACCEPTED.toString())) {
            postsPageable = postRepository.getAcceptedPostsForModer(currentUserID, pageable);
        } else if (moderationStatus.equalsIgnoreCase(ModerationStatus.DECLINED.toString())) {
            postsPageable = postRepository.getDeclinedPostsForModer(currentUserID, pageable);
        }

        Long countAllPosts = Objects.requireNonNull(postsPageable).getTotalElements();
        List<PostDTO> postDTOs = postToDTOCustomMapper.convertToDTO(postsPageable);

        return PostsResponse.create()
                .setCount(countAllPosts)
                .setPosts(postDTOs);
    }

    public ModerateResponse moderatePost(ModerateRequest request) {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findModeratorByEmail(currentUserEmail);

        Post post = postRepository.getPostByID(request.getPostID());

        if (request.getDecision().equalsIgnoreCase(ModeratePostStatus.ACCEPT.toString())) {
            post.setModerationStatus(ModerationStatus.ACCEPTED);

        } else if (request.getDecision().equalsIgnoreCase(ModeratePostStatus.DECLINE.toString())) {
            post.setModerationStatus(ModerationStatus.DECLINED);

        } else {
            ModerateResponse response = new ModerateResponse();
            response.setResult(false);
            return response;
        }

        post.setModerator(currentUser);
        postRepository.save(post);

        ModerateResponse response = new ModerateResponse();
        response.setResult(true);
        return response;
    }

    public PostResponse getPostByID(Integer postID) {

        Post post = postRepository.getPostByID(postID);

        PostDTO postDTO = postToDTOCustomMapper.convertToDTO(post);

        List<String> tags = tagRepository.getTagNamesByPostID(postID);

        PostResponse response = PostResponse.create();

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

    public LikeResponse likePost(LikeRequest request) {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        Integer postID = request.getPostID();

        Post post = postRepository.getPostByID(postID);

        PostVote postVote = postVoteRepository.getVote(currentUser.getId(), postID);

        if (postVote != null && !postVote.isLike()) {
            postVote.setLike(true);
            postVoteRepository.save(postVote);

            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setResult(true);
            return likeResponse;

        } else if (postVote != null && postVote.isLike()) {

            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setResult(false);
            return likeResponse;
        } else {
            PostVote dislike = new PostVote();
            dislike.setUser(currentUser);
            dislike.setPost(post);
            dislike.setTime(LocalDateTime.now());
            dislike.setLike(true);
            postVoteRepository.save(dislike);

            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setResult(true);
            return likeResponse;
        }
    }

    public LikeResponse dislikePost(LikeRequest request) {

        String currentUserEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User currentUser = userRepository.findUserByEmail(currentUserEmail);

        Integer postID = request.getPostID();

        Post post = postRepository.getPostByID(postID);

        PostVote postVote = postVoteRepository.getVote(currentUser.getId(), postID);

        if (postVote != null && postVote.isLike()) {
            postVote.setLike(false);
            postVoteRepository.save(postVote);

            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setResult(true);
            return likeResponse;
        } else if (postVote != null && !postVote.isLike()) {

            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setResult(false);
            return likeResponse;
        } else {
            PostVote dislike = new PostVote();
            dislike.setUser(currentUser);
            dislike.setPost(post);
            dislike.setTime(LocalDateTime.now());
            dislike.setLike(false);
            postVoteRepository.save(dislike);

            LikeResponse likeResponse = new LikeResponse();
            likeResponse.setResult(dislike.isLike());
            return likeResponse;
        }
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

            PostErrorDTO titleError = new PostErrorDTO();
            titleError.setTitle(POST_TITLE_SHORT_ERROR);
            List<PostErrorDTO> errorDTOS = new ArrayList<>();
            errorDTOS.add(titleError);

            response.setResult(false);
            response.setErrors(errorDTOS);
            return response;
        }

        if (request.getText().length() < MIN_POST_BODY_LENGTH) {

            PostErrorDTO titleError = new PostErrorDTO();
            titleError.setTitle(POST_BODY_SHORT_ERROR);
            List<PostErrorDTO> errorDTOS = new ArrayList<>();
            errorDTOS.add(titleError);

            response.setResult(false);
            response.setErrors(errorDTOS);

            return response;
        }

        response.setResult(true);

        return response;
    }

}
