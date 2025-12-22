package se.jensen.saman.socialnetworkmaven.dto;

public record UserResponseChangeProfileDTO(Long id, String username,
                                           String displayName,
                                           String bio,
                                           String profileImagePath) {
}
