package se.jensen.saman.socialnetworkmaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import se.jensen.saman.socialnetworkmaven.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

   Optional<User>  findUserByUsername(String username);

    boolean existsByEmail (String email);

    User getByUsername(String username);
}
