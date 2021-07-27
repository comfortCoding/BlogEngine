package main.controller;

import main.api.response.PostResponse;
import main.api.response.PostsResponse;
import main.config.exception.NotFoundException;
import main.services.ApiPostService;
import org.springframework.http.HttpStatus;
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

    @GetMapping(value = "/byDate")
    public ResponseEntity<?> searchByDate() {
        if (true) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/byTag")
    public ResponseEntity<?> searchByTag() {
        if (true) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
