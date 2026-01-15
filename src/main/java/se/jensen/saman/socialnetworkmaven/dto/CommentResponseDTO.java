package se.jensen.saman.socialnetworkmaven.dto;

import java.time.LocalDateTime;

public record CommentResponseDTO(Long id,
                                 String text,
                                 LocalDateTime createdAt,
                                 Long postId,
                                 Long userId) {
}
