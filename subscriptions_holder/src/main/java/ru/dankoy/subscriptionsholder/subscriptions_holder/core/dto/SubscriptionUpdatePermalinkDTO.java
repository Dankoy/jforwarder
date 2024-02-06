package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUpdatePermalinkDTO {

  @Min(1)
  private long id;

  @NotEmpty
  private String lastPermalink;


  public static SubscriptionUpdatePermalinkDTO toDTO(Subscription subs) {

    return new SubscriptionUpdatePermalinkDTO(
        subs.getId(),
        subs.getLastPermalink()
    );

  }

  public static Subscription fromDTO(SubscriptionUpdatePermalinkDTO dto) {

    return Subscription.builder()
        .id(dto.getId())
        .lastPermalink(dto.getLastPermalink())
        .build();
  }


}
