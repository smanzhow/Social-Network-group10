package se.jensen.saman.socialnetworkmaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.saman.socialnetworkmaven.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
