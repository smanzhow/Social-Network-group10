package se.jensen.saman.socialnetworkmaven.service;

import jakarta.validation.Valid;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.saman.socialnetworkmaven.dto.CommentRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.CommentResponseDTO;
import se.jensen.saman.socialnetworkmaven.mapper.CommentMapper;
import se.jensen.saman.socialnetworkmaven.model.Comment;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.repository.CommentRepository;
import se.jensen.saman.socialnetworkmaven.repository.PostRepository;
import se.jensen.saman.socialnetworkmaven.repository.UserRepository;

import java.time.LocalDateTime;


@Service
@Transactional
public class CommentService {


    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, PostRepository postRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;

        this.commentMapper = commentMapper;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public CommentResponseDTO postCommentOnPost(Long postId, CommentRequestDTO reqDTO){
        Post post = postRepository.findById(postId)
                .orElseThrow( ()-> new OpenApiResourceNotFoundException("Post not found"));

        Comment comment = commentMapper.fromDTO(reqDTO);

        User user = userRepository.findById(reqDTO.userId())
                .orElseThrow(()-> new OpenApiResourceNotFoundException("User not found"));

        comment.setPost(post);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

         Comment savedComment = commentRepository.save(comment);

         return commentMapper.fromEntity(savedComment);


//skriv ut steg för steg, vad behöver ske snarare,på papper, sedan gör.

    }


}
