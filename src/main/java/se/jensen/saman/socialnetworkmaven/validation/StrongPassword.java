package se.jensen.saman.socialnetworkmaven.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StrongPasswordValidator.class)
public @interface StrongPassword {

    String message() default
    "Password has to include atleast one capital letter, one small and one number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
