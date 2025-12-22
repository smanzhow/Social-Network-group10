package se.jensen.saman.socialnetworkmaven.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestChangeProfileDTO(@NotBlank (message = "Bio can't be empty.")
                                          @Size (max = 2000, message = "Max 2000 characters.")
                                          String bio,
                                          @NotBlank (message = "Nickname can't be empty.")
                                          @Size (min = 5 ,max = 60, message = "Nickname has to be between 10 and 60 characters.")
                                          String displayName,
                                          @NotBlank (message = "Profile pic can't be empty")
                                          String profileImagePath) {
}
