package se.jensen.saman.socialnetworkmaven.dto;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record CommentRequestDTO(@NotBlank(message = "Text can't be empty")
                                @Size(min = 1, max = 800)
                                String text,
                                @NotNull(message = "User ID must be provided")
                                Long userId) {
}
