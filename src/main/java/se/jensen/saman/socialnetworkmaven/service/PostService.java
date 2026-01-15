package se.jensen.saman.socialnetworkmaven.service;



import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.saman.socialnetworkmaven.dto.PostRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.PostResponseDTO;
import se.jensen.saman.socialnetworkmaven.mapper.PostMapper;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.repository.PostRepository;
import se.jensen.saman.socialnetworkmaven.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, PostMapper postMapper, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.userRepository = userRepository;
    }


    public List<PostResponseDTO> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::postToRespDTO)
                .toList();

    }

    public PostResponseDTO createPost(String username, PostRequestDTO reqDTO) {

        User author = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        Post post = postMapper.reqDtoToPost(reqDTO, author);
        Post savedPost = postRepository.save(post);

        return postMapper.postToRespDTO(savedPost);
    }

    public PostResponseDTO getPostById(Long id) {

        return postRepository.findById(id)
                .map(postMapper::postToRespDTO)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Post not found"));
    }


    public PostResponseDTO updatePost(Long id, String username, PostRequestDTO reqDTO) {
        Post post = getPostAndVerifyOwner(id, username);
        if (post.getUpdatedAt() != null) {
            throw new AccessDeniedException("Cannot edit more than once.");
        }
        postMapper.postUpdateFromReqDTO(reqDTO, post);
        Post savedPost = postRepository.save(post);

        return postMapper.postToRespDTO(savedPost);
    }


    public void deletePost(Long id, String username) {
        getPostAndVerifyOwner(id, username);
        postRepository.deleteById(id);

    }

    private Post getPostAndVerifyOwner(Long id, String username) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Post not found."));
        if (!post.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
        return post;
    }


    public Page<PostResponseDTO> getPosts(Long userId, Pageable pageable){
        Page<Post> postPage;
        if (userId != null){
           postPage =  postRepository.findByUserId(userId, pageable);
        } else {
            postPage = postRepository.findAll(pageable);
        }
        return postPage.map(postMapper::postToRespDTO);
    }

    public Page<PostResponseDTO> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(post ->
                        new PostResponseDTO(post.getId(),
                                post.getText(),
                                post.getCreatedAt(),
                                post.getUpdatedAt(),
                                post.getUser().getUsername(),
                                post.getUser().getId()));
    }

//Long id,
//                              String text,
//                              LocalDateTime createdAt,
//                              LocalDateTime updatedAt,
//                              String authorUsername,
//                              Long userId

}
