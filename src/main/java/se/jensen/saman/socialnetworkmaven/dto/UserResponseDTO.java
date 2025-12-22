package se.jensen.saman.socialnetworkmaven.dto;

public record UserResponseDTO(Long id,
                              String username,
                              String role,
                              String email,
                              String displayName,
                              String bio,
                              String profileImagePath) {
}
