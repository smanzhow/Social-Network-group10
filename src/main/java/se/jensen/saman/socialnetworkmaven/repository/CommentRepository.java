package se.jensen.saman.socialnetworkmaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.saman.socialnetworkmaven.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
