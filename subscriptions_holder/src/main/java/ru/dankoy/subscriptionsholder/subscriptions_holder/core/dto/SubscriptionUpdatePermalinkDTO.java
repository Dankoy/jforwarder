package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionUpdatePermalinkDTO {

  @Valid
  @NotNull
  private CommunityTelegramChatPKCreateDTO communityChat;

  @NotEmpty
  private String lastPermalink;


  public static SubscriptionUpdatePermalinkDTO toDTO(Subscription subscription) {

    return SubscriptionUpdatePermalinkDTO.builder()
        .communityChat(CommunityTelegramChatPKCreateDTO.toDTO(subscription.getCommunityChat()))
        .lastPermalink(subscription.getLastPermalink())
        .build();

  }

  public static Subscription fromDTO(SubscriptionUpdatePermalinkDTO dto) {

    return new Subscription(
        CommunityTelegramChatPKCreateDTO.fromDTO(dto.getCommunityChat()),
        dto.getLastPermalink()
    );

  }


}
