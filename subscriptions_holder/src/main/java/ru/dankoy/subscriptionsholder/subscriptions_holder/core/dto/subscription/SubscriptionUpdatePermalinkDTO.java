package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class SubscriptionUpdatePermalinkDTO {

  @Valid
  @NotNull
  private SubscriptionCreateCommunityDTO community;

  @Valid
  @NotNull
  private SectionDTO section;

  @Valid
  @NotNull
  private ChatCreateDTO chat;

  @NotEmpty
  private String lastPermalink;


  public static SubscriptionUpdatePermalinkDTO toDTO(CommunitySubscription communitySubscription) {

    return SubscriptionUpdatePermalinkDTO.builder()
        .community(SubscriptionCreateCommunityDTO.toDTO(communitySubscription.getCommunity()))
        .section(SectionDTO.toDTO(communitySubscription.getSection()))
        .chat(ChatCreateDTO.toDTO(communitySubscription.getChat()))
        .lastPermalink(communitySubscription.getLastPermalink())
        .build();

  }

  public static CommunitySubscription fromDTO(SubscriptionUpdatePermalinkDTO dto) {

    return new CommunitySubscription(
        0,
        SubscriptionCreateCommunityDTO.fromDTO(dto.getCommunity()),
        SectionDTO.fromDTO(dto.getSection()),
        ChatCreateDTO.fromDTO(dto.getChat()),
        dto.getLastPermalink()
    );

  }


}
