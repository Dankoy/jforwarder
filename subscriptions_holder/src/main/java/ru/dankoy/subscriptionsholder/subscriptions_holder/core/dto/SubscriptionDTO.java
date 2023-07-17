package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;


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
public class SubscriptionDTO {

  private CommunityTelegramChatPKDTO communityChat;

  private String lastPermalink;


  public static SubscriptionDTO toDTO(Subscription subscription) {

    return SubscriptionDTO.builder()
        .communityChat(CommunityTelegramChatPKDTO.toDTO(subscription.getCommunityChat()))
        .lastPermalink(subscription.getLastPermalink())
        .build();

  }

  public static Subscription fromDTO(SubscriptionDTO dto) {

    return new Subscription(
        CommunityTelegramChatPKDTO.fromDTO(dto.getCommunityChat()),
        dto.getLastPermalink()
    );

  }


}
