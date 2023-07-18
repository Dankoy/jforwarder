package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Subscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.SectionDTO;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionCreateDTO {

  @Valid
  @NotNull
  private SubscriptionCreateCommunityDTO community;

  @Valid
  @NotNull
  private SectionDTO section;

  @Valid
  @NotNull
  private ChatCreateDTO chat;

  private String lastPermalink;


  public static SubscriptionCreateDTO toDTO(Subscription subscription) {

    return SubscriptionCreateDTO.builder()
        .community(SubscriptionCreateCommunityDTO.toDTO(subscription.getCommunity()))
        .section(SectionDTO.toDTO(subscription.getSection()))
        .chat(ChatCreateDTO.toDTO(subscription.getChat()))
        .lastPermalink(subscription.getLastPermalink())
        .build();

  }

  public static Subscription fromDTO(SubscriptionCreateDTO dto) {

    return new Subscription(
        0,
        SubscriptionCreateCommunityDTO.fromDTO(dto.getCommunity()),
        SectionDTO.fromDTO(dto.getSection()),
        ChatCreateDTO.fromDTO(dto.getChat()),
        dto.getLastPermalink()
    );

  }


}
