package se.jensen.saman.socialnetworkmaven.mapper;

import org.mapstruct.Mapper;
import se.jensen.saman.socialnetworkmaven.dto.CommentRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.CommentResponseDTO;
import se.jensen.saman.socialnetworkmaven.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment fromDTO (CommentRequestDTO commentRequestDTO);

    CommentResponseDTO fromEntity (Comment comment);
}
