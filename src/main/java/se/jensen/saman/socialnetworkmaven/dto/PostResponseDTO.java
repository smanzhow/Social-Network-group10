package se.jensen.saman.socialnetworkmaven.dto;

import java.time.LocalDateTime;

public record PostResponseDTO(Long id,
                              String text,
                              LocalDateTime createdAt,
                              LocalDateTime updatedAt,
                              String authorUsername,
                              Long userId) {
}
