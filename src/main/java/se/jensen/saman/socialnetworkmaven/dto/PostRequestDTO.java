package se.jensen.saman.socialnetworkmaven.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;



public record PostRequestDTO(@NotBlank(message = "Text får inte vara tom.")
                             @Size(min =3, max = 4000, message = "Texten får minst vara 3 karaktärer och max 4000")
                             String text){
}
