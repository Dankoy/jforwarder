package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.validator.subscriptiontype.tag;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class TagSubscriptionTypeValidator
    implements ConstraintValidator<TagSubscriptionTypeConstraint, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    return Objects.nonNull(value) && (value.equals("tag"));
  }
}
