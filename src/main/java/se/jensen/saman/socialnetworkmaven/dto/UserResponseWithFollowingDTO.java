package se.jensen.saman.socialnetworkmaven.dto;

import java.util.List;

public record UserResponseWithFollowingDTO(String username,
                                           List<UserFollowingDTO> following) {
}
