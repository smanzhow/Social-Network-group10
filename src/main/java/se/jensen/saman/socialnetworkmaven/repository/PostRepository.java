package se.jensen.saman.socialnetworkmaven.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.model.User;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByUserId(Long id, Pageable pageable);

    Pageable user(User user);
}
