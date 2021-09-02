package main.controller;

import main.api.request.PostDataRequest;
import main.api.response.PostDataResponse;
import main.api.response.PostResponse;
import main.api.response.PostsResponse;
import main.config.exception.NotFoundException;
import main.services.ApiPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    private final ApiPostService apiPostService;

    public ApiPostController(ApiPostService apiPostService) {
        this.apiPostService = apiPostService;
    }

    @GetMapping
    public ResponseEntity<PostsResponse> getPosts(@RequestParam(defaultValue = "0") Integer offset,
                                                  @RequestParam(defaultValue = "10") Integer limit,
                                                  @RequestParam(defaultValue = "recent") String mode) throws NotFoundException {
        PostsResponse response = apiPostService.getPosts(offset, limit, mode);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PostsResponse> searchPosts(@RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "10") Integer limit,
                                                     @RequestParam(name = "query") String searchText) throws NotFoundException {
        PostsResponse response = apiPostService.searchPosts(offset, limit, searchText);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{ID}")
    public ResponseEntity<PostResponse> getPostByID(@PathVariable(name = "ID") Integer postID) {
        PostResponse response = apiPostService.getPostByID(postID);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PostDataResponse> addPost(@RequestBody PostDataRequest request) {
        PostDataResponse response = apiPostService.addPost(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{ID}")
    public ResponseEntity<PostDataResponse> updatePost(@PathVariable(name = "ID") Integer postID,
                                                       @RequestBody PostDataRequest request) {
        PostDataResponse response = apiPostService.updatePost(postID, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/byDate")
    public ResponseEntity<PostsResponse> searchByDate(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                      @RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                                      @RequestParam(name = "date") String date) {
        PostsResponse response = apiPostService.searchByDate(offset, limit, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/byTag")
    public ResponseEntity<PostsResponse> searchByTag(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                     @RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                                     @RequestParam(name = "tag") String tag) {
        PostsResponse response = apiPostService.searchByTag(offset, limit, tag);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/my")
    public ResponseEntity<PostsResponse> getPostsByPerson(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                          @RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                                          @RequestParam(name = "status") String postStatus) {
        PostsResponse response = apiPostService.getPostsByPerson(offset, limit, postStatus);
        return ResponseEntity.ok(response);
    }
}
