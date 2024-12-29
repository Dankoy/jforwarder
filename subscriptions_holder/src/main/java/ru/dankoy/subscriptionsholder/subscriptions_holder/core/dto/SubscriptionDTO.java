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
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatDTO;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

  @Min(1)
  private long id;

  @NotEmpty private String lastPermalink;

  @NotNull private ChatDTO chat;

  @NotNull private LocalDateTime createdAt;

  @NotNull private LocalDateTime modifiedAt;

  public static SubscriptionDTO toDTO(Subscription subs) {

    return new SubscriptionDTO(
        subs.getId(),
        subs.getLastPermalink(),
        ChatDTO.toDTO(subs.getChat()),
        subs.getCreatedAt(),
        subs.getModifiedAt());
  }

  public static Subscription fromDTO(SubscriptionDTO dto) {

    return Subscription.builder()
        .id(dto.getId())
        .lastPermalink(dto.getLastPermalink())
        .chat(ChatDTO.fromDTO(dto.getChat()))
        .createdAt(dto.getCreatedAt())
        .modifiedAt(dto.getModifiedAt())
        .build();
  }
}
