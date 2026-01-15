package se.jensen.saman.socialnetworkmaven.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import se.jensen.saman.socialnetworkmaven.dto.PostRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.PostResponseDTO;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.model.User;

import java.time.LocalDateTime;

@Mapper (componentModel = "spring",
        imports = LocalDateTime.class)
public interface PostMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", expression = "java(LocalDateTime.now())")
    Post reqDtoToPost(PostRequestDTO postRequestDTO, User user);


    @Mapping(source = "user.username", target = "authorUsername")
    @Mapping(source = "user.id", target = "userId")
    PostResponseDTO postToRespDTO(Post post);

    @Mapping(target = "updatedAt", expression = "java(LocalDateTime.now())")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    void postUpdateFromReqDTO(PostRequestDTO postRequestDTO, @MappingTarget Post post);
}
