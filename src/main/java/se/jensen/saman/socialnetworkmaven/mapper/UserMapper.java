package se.jensen.saman.socialnetworkmaven.mapper;

import org.mapstruct.*;
import se.jensen.saman.socialnetworkmaven.dto.*;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.model.UserFollow;

@Mapper(componentModel = "spring",
        uses = PostMapper.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "role", constant = "USER")
    User fromReqDTOtoUser(UserRequestDTO reqDTO);

    UserResponseDTO fromUserToResponseDTO(User user);


    UserResponseWithFollowersDTO fromUserToUserWithFollowersDTO(User user);


    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "password", ignore = true)
    void updateUserFromReqDTO(UserRequestDTO userReqDTO, @MappingTarget User user);


    @Mapping(source = "token", target = "token")
    UserLoginResponseDTO loginFromUserToResponseDTO(User user, String token);


    @Mapping(source = "newPassword", target = "password")
    void changePasswordUpdate(UserRequestPasswordChangeDTO requestPasswordChangeDTO, @MappingTarget User user);


    void changeProfileUpdate(UserRequestChangeProfileDTO dto, @MappingTarget User user);


    UserResponseChangeProfileDTO userToProfileResponseDTO(User user);

    @Mapping(source = "follower.id", target = "id")
    @Mapping(source = "follower.username", target = "username")
    UserFollowersDTO mapFollowerToDTO(UserFollow userFollow);

    // Denna körs för varje item i "followingList"
    // Vi hämtar ID och Username från "followed" (personen jag följer)
    @Mapping(source = "followed.id", target = "id")
    @Mapping(source = "followed.username", target = "username")
    UserFollowingDTO mapFollowingToDTO(UserFollow userFollow);
}

