package se.jensen.saman.socialnetworkmaven.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public record HabitRequestDTO(
                             @NotNull
                             @NotBlank
                             String habitName,
                              @NotNull
                              String description
                              ) {
}
