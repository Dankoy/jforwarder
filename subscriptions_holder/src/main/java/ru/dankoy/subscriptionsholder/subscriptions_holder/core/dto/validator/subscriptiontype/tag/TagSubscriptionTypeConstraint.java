package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.validator.subscriptiontype.tag;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagSubscriptionTypeValidator.class)
public @interface TagSubscriptionTypeConstraint {

    String message() default "{sendmessage.parseMode}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
