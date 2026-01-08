package se.jensen.saman.socialnetworkmaven.dto;

import se.jensen.saman.socialnetworkmaven.model.Habit;

import java.util.List;

public record UserWithHabitsResponseDTO (Long id,
                                         String username,
                                         String role,
                                         String displayName,
                                         String profileImagePath,
                                         String bio,
                                         List<HabitResponseDTO> habitList){
}
