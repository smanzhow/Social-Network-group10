package se.jensen.saman.socialnetworkmaven.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import se.jensen.saman.socialnetworkmaven.validation.StrongPassword;

public record UserRequestPasswordChangeDTO(@Schema(example = "Current password")
                                           @NotBlank
                                           String oldPassword,
                                           @StrongPassword
                                           @NotBlank(message = "Password cant be empty")
                                           @Size(min = 1, max = 40, message = "Password has to between 1 and 40 characters.")
                                           String newPassword) {
}
