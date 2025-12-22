package se.jensen.saman.socialnetworkmaven.dto;

public record UserLoginResponseDTO(Long id,
                                   String username,
                                   String role,
                                   String token) {
}
