package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.ScopeCreateDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TypeCreateDTO;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelSubscriptionCreateDTO {

  @Valid @NotNull private ChannelCreateDTO channel;

  @Valid @NotNull private ChatCreateDTO chat;

  @Valid @NotNull private OrderCreateDTO order;

  @Valid @NotNull private ScopeCreateDTO scope;

  @Valid @NotNull private TypeCreateDTO type;

  private String lastPermalink;

  public static ChannelSubscriptionCreateDTO toDTO(ChannelSub channelSub) {

    return new ChannelSubscriptionCreateDTO(
        ChannelCreateDTO.toDTO(channelSub.getChannel()),
        ChatCreateDTO.toDTO(channelSub.getChat()),
        OrderCreateDTO.toDTO(channelSub.getOrder()),
        ScopeCreateDTO.toDTO(channelSub.getScope()),
        TypeCreateDTO.toDTO(channelSub.getType()),
        channelSub.getLastPermalink());
  }

  public static ChannelSub fromDTO(ChannelSubscriptionCreateDTO dto) {
    return ChannelSub.builder()
        .id(0)
        .channel(ChannelCreateDTO.fromDTO(dto.getChannel()))
        .chat(ChatCreateDTO.fromDTO(dto.getChat()))
        .order(OrderCreateDTO.fromDTO(dto.getOrder()))
        .scope(ScopeCreateDTO.fromDTO(dto.getScope()))
        .type(TypeCreateDTO.fromDTO(dto.getType()))
        .lastPermalink(dto.getLastPermalink())
        .build();
  }
}
