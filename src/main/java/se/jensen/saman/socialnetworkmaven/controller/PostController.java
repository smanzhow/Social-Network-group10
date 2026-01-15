package se.jensen.saman.socialnetworkmaven.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.jensen.saman.socialnetworkmaven.dto.PostRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.PostResponseDTO;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.service.PostService;

import java.util.List;


@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:5173")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;

    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getAllPosts() {
        List<PostResponseDTO> responseDTOList = postService.getAllPosts();

        return ResponseEntity.ok(responseDTOList);
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@AuthenticationPrincipal String username, @Valid @RequestBody PostRequestDTO reqDTO) {
        logger.info("Trying to create a post with the text length : {}", reqDTO.text().length());
        PostResponseDTO respDTO = postService.createPost(username, reqDTO);
        logger.info("Successfully created a post by User with Username: {}", username);

        return ResponseEntity.status(HttpStatus.CREATED).body(respDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @AuthenticationPrincipal String username, @Valid @RequestBody PostRequestDTO reqDTO) {
        logger.info("Trying to update post with logged in as: {}", username);
        PostResponseDTO responseDTO = postService.updatePost(id, username, reqDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, @AuthenticationPrincipal String username) {
        postService.deletePost(id, username);
        logger.warn("Deleted post with id: {}", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<PostResponseDTO>> getPosts(@RequestParam(required = false) Long userId,
            @PageableDefault(size = 10,
            sort = "createdAt",
            direction = Sort.Direction.DESC) Pageable pageable) {

        Page<PostResponseDTO> respDTO = postService.getPosts(userId, pageable);


        return ResponseEntity.ok(respDTO);

    }

}
