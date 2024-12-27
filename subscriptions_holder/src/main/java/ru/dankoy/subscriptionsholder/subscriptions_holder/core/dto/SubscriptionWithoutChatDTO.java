package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.Subscription;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionWithoutChatDTO {

  @Min(1)
  private long id;

  @NotEmpty private String lastPermalink;

  @NotNull private LocalDateTime createdAt;

  @NotNull private LocalDateTime modifiedAt;

  public static SubscriptionWithoutChatDTO toDTO(Subscription subs) {

    return new SubscriptionWithoutChatDTO(
        subs.getId(), subs.getLastPermalink(), subs.getCreatedAt(), subs.getModifiedAt());
  }
}
