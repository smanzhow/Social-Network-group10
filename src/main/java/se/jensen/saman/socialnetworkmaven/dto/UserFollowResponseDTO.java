package se.jensen.saman.socialnetworkmaven.dto;

import java.time.LocalDateTime;

public record UserFollowResponseDTO(Long id,
                                    LocalDateTime createdAt,
                                    Long followedId,
                                    Long followerId) {
}
