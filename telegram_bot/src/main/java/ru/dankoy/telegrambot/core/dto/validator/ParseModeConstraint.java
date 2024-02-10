package ru.dankoy.telegrambot.core.dto.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ParseModeValidator.class)
public @interface ParseModeConstraint {

  String message() default "{sendmessage.parseMode}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
