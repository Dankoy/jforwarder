package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.SubscriptionType;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.validator.subscriptiontype.tag.TagSubscriptionTypeConstraint;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionTypeCreateDTO {

  @NotNull @NotEmpty @TagSubscriptionTypeConstraint private String type;

  public static SubscriptionTypeCreateDTO toDTO(SubscriptionType type) {

    return new SubscriptionTypeCreateDTO(type.getType());
  }

  public static SubscriptionType fromDTO(SubscriptionTypeCreateDTO dto) {

    return new SubscriptionType(0, dto.getType());
  }
}
