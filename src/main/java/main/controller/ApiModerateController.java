package main.controller;

import main.api.request.ModerateRequest;
import main.api.response.ModerateResponse;
import main.api.response.PostsResponse;
import main.services.ApiPostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiModerateController {

    private final ApiPostService apiPostService;

    public ApiModerateController(ApiPostService apiPostService) {
        this.apiPostService = apiPostService;
    }

    @PostMapping(value = "/moderation")
    public ResponseEntity<ModerateResponse> moderatePost(@RequestBody ModerateRequest request) {
        ModerateResponse response = apiPostService.moderatePost(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/post/moderation")
    public ResponseEntity<PostsResponse> getPostsForModeration(@RequestParam(name = "offset", defaultValue = "0") Integer offset,
                                                               @RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                                               @RequestParam(name = "status") String moderationStatus) {

        PostsResponse response = apiPostService.getPostsForModeration(offset, limit, moderationStatus);
        return ResponseEntity.ok(response);
    }

}
