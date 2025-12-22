package se.jensen.saman.socialnetworkmaven.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDTO(@NotBlank(message = "Username can't be empty.")
                                  String username,
                                  @NotBlank(message = "Password can't be empty.")
                                  @Size(min = 6, message = "Password has to be atleast 6 characters.")
                                  String password){
}
