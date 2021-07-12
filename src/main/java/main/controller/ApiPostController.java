package main.controller;

import main.api.response.PostsResponse;
import main.config.Config;
import main.config.exception.NotFoundException;
import main.model.DTO.PostDTO;
import main.services.ApiPostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

        Map<Long, List<PostDTO>> result = apiPostService.getPosts(offset, limit, mode);

        if (result.size() != 1) {
            throw new NotFoundException(Config.ERROR_INCORRECT_HASHMAP_POSTS);
        }

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();

        for (Map.Entry<Long, List<PostDTO>> entry : result.entrySet()) {
            response.setCount(entry.getKey());
            response.setPosts(entry.getValue());
        }

        return ResponseEntity
                .ok(response);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<PostsResponse> searchPosts(@RequestParam(defaultValue = "0") Integer offset,
                                                     @RequestParam(defaultValue = "10") Integer limit,
                                                     @RequestParam(name = "query") String searchText) throws NotFoundException {

        Map<Long, List<PostDTO>> result = apiPostService.searchPosts(offset, limit, searchText);

        if (result.size() != 1) {
            throw new NotFoundException(Config.ERROR_INCORRECT_HASHMAP_POSTS);
        }

        //Сформируем ответ для фронта
        PostsResponse response = new PostsResponse();

        for (Map.Entry<Long, List<PostDTO>> entry : result.entrySet()) {
            response.setCount(entry.getKey());
            response.setPosts(entry.getValue());
        }

        return ResponseEntity
                .ok(response);
    }

    @GetMapping(value = "/{ID}")
    public ResponseEntity<?> getPostByID(@PathVariable(name = "id") Integer postID) {
        return apiPostService.getPostByID(postID);
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
