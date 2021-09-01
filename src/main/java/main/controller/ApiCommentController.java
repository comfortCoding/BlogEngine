package main.controller;

import main.api.request.CommentRequest;
import main.api.response.CommentResponse;
import main.services.ApiCommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
public class ApiCommentController {

    private final ApiCommentService apiCommentService;

    public ApiCommentController(ApiCommentService apiCommentService) {
        this.apiCommentService = apiCommentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request){
        CommentResponse response = apiCommentService.addComment(request);
        return ResponseEntity.ok(response);
    }


}
