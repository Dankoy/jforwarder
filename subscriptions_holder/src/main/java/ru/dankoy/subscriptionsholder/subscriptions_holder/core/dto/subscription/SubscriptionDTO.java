package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.CommunitySubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SectionDTO;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionDTO {

  private long id;

  private CommunityWithoutSectionsDTO community;

  private SectionDTO section;

  private ChatDTO chat;

  private String lastPermalink;


  public static SubscriptionDTO toDTO(CommunitySubscription communitySubscription) {

    return SubscriptionDTO.builder()
        .id(communitySubscription.getId())
        .community(CommunityWithoutSectionsDTO.toDTO(communitySubscription.getCommunity()))
        .section(SectionDTO.toDTO(communitySubscription.getSection()))
        .chat(ChatDTO.toDTO(communitySubscription.getChat()))
        .lastPermalink(communitySubscription.getLastPermalink())
        .build();

  }

  public static CommunitySubscription fromDTO(SubscriptionDTO dto) {

    return new CommunitySubscription(
        dto.getId(),
        CommunityWithoutSectionsDTO.fromDTO(dto.getCommunity()),
        SectionDTO.fromDTO(dto.getSection()),
        ChatDTO.fromDTO(dto.getChat()),
        dto.getLastPermalink()
    );

  }


}
