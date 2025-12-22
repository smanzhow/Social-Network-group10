package se.jensen.saman.socialnetworkmaven.dto;

import java.util.List;

public record UserWithPostsResponseDTO(Long id,
                                       String username,
                                       String role,
                                       String displayName,
                                       String profileImagePath,
                                       String bio,
                                       List<PostResponseDTO> posts) {
}
