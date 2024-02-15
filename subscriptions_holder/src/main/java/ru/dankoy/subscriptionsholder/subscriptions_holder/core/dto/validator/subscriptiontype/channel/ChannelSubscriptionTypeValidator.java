package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.validator.subscriptiontype.channel;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class ChannelSubscriptionTypeValidator
    implements ConstraintValidator<ChannelSubscriptionTypeConstraint, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    return Objects.nonNull(value) && (value.equals("channel"));
  }
}
