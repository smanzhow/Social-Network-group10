package se.jensen.saman.socialnetworkmaven.dto;




import java.util.List;
import java.util.Set;

public record UserResponseWithFollowersDTO(String username,
                                           List<UserFollowersDTO> followers) {
}
