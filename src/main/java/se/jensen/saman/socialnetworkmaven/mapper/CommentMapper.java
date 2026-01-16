package se.jensen.saman.socialnetworkmaven.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.jensen.saman.socialnetworkmaven.dto.CommentRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.CommentResponseDTO;
import se.jensen.saman.socialnetworkmaven.model.Comment;

@Mapper(componentModel = "spring",
        uses = UserMapper.class, imports = PostMapper.class)
public interface CommentMapper {

    Comment fromDTO (CommentRequestDTO commentRequestDTO);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "post.id", target = "postId")
    CommentResponseDTO fromEntity (Comment comment);
}
