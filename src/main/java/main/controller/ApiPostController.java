package main.controller;

import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/post")
public class ApiPostController {

    @Autowired
    private PostRepository postRepository;

    @GetMapping(value = "/")
    public ResponseEntity<?> getPosts() {
        if (true) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchPosts() {
        if (true) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
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
