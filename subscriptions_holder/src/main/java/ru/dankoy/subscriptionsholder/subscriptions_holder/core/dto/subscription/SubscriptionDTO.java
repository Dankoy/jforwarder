package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.CommunityDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SectionDTO;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

  private long id;

  private CommunityDTO community;

  private SectionDTO section;

  private ChatDTO chat;

  private String lastPermalink;


  public static SubscriptionDTO toDTO(Subscription subscription) {

    return SubscriptionDTO.builder()
        .community(CommunityDTO.toDTO(subscription.getCommunity()))
        .section(SectionDTO.toDTO(subscription.getSection()))
        .chat(ChatDTO.toDTO(subscription.getChat()))
        .lastPermalink(subscription.getLastPermalink())
        .build();

  }

  public static Subscription fromDTO(SubscriptionDTO dto) {

    return new Subscription(
        dto.getId(),
        CommunityDTO.fromDTO(dto.getCommunity()),
        SectionDTO.fromDTO(dto.getSection()),
        ChatDTO.fromDTO(dto.getChat()),
        dto.getLastPermalink()
    );

  }


}
