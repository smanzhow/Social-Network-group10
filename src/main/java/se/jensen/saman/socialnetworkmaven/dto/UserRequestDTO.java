package se.jensen.saman.socialnetworkmaven.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import se.jensen.saman.socialnetworkmaven.validation.StrongPassword;

public record UserRequestDTO(
                             @Schema(example = "username:)")
                             @NotBlank(message = "Username can't be empty.")
                             @Size(min = 1, max = 30, message = "Username has to be between 1 and 30 characters.")
                             String username,
                             @Schema(example = "Tjenare1")
                             @StrongPassword
                             @NotBlank(message = "Password cant be empty")
                             @Size(min = 1, max = 40, message = "Password has to between 1 and 40 characters.")
                             String password,
                             @Schema(example = "kalle@example.com")
                             @NotBlank(message = "Email cant be empty.")
                             @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                                     message = "Email must be valid and contain a domain (e.g., .com)")
                             @Email(message = "Must be a valid email.")
                             String email){

}
