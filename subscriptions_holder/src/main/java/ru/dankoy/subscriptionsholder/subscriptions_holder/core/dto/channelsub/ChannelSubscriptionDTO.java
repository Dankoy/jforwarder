package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.channelsub;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.ChannelSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.OrderDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.ScopeDTO;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription.TypeDTO;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelSubscriptionDTO {

  private long id;

  private ChannelDTO channel;

  private ChatDTO chat;

  private OrderDTO order;

  private ScopeDTO scope;

  private TypeDTO type;

  private String lastPermalink;

  private LocalDateTime createdAt;
  
  private LocalDateTime modifiedAt;

  public static ChannelSubscriptionDTO toDTO(ChannelSub channelSub) {

    return new ChannelSubscriptionDTO(
        channelSub.getId(),
        ChannelDTO.toDTO(channelSub.getChannel()),
        ChatDTO.toDTO(channelSub.getChat()),
        OrderDTO.toDTO(channelSub.getOrder()),
        ScopeDTO.toDTO(channelSub.getScope()),
        TypeDTO.toDTO(channelSub.getType()),
        channelSub.getLastPermalink(),
        channelSub.getCreatedAt(),
        channelSub.getModifiedAt());
  }

  public static ChannelSub fromDTO(ChannelSubscriptionDTO dto) {

    return ChannelSub.builder()
        .id(0)
        .channel(ChannelDTO.fromDTO(dto.getChannel()))
        .chat(ChatDTO.fromDTO(dto.getChat()))
        .order(OrderDTO.fromDTO(dto.getOrder()))
        .scope(ScopeDTO.fromDTO(dto.getScope()))
        .type(TypeDTO.fromDTO(dto.getType()))
        .lastPermalink(dto.getLastPermalink())
        .createdAt(dto.getCreatedAt())
        .modifiedAt(dto.getModifiedAt())
        .build();
  }
}
