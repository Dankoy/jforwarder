package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.sentcoubsregistry;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;


@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateDTO {

  @Valid
  @Min(1)
  private long id;

  public static SubscriptionCreateDTO toDTO(Subscription subscription) {

    return SubscriptionCreateDTO.builder()
        .id(subscription.getId())
        .build();

  }

  public static Subscription fromDTO(SubscriptionCreateDTO dto) {

    return Subscription.builder()
        .id(dto.getId())
        .build();

  }

}
