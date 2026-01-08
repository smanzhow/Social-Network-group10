package se.jensen.saman.socialnetworkmaven.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.jensen.saman.socialnetworkmaven.model.Habit;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
