package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatDTO;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscriptionDTO {

  private long id;

  private TagDTO tag;

  private ChatDTO chat;

  private OrderDTO order;

  private ScopeDTO scope;

  private TypeDTO type;

  private String lastPermalink;

  public static TagSubscriptionDTO toDTO(TagSub tagSubscription) {

    return new TagSubscriptionDTO(
        tagSubscription.getId(),
        TagDTO.toDTO(tagSubscription.getTag()),
        ChatDTO.toDTO(tagSubscription.getChat()),
        OrderDTO.toDTO(tagSubscription.getOrder()),
        ScopeDTO.toDTO(tagSubscription.getScope()),
        TypeDTO.toDTO(tagSubscription.getType()),
        tagSubscription.getLastPermalink());
  }

  public static TagSub fromDTO(TagSubscriptionDTO dto) {

    return TagSub.builder()
        .id(0)
        .tag(TagDTO.fromDTO(dto.getTag()))
        .chat(ChatDTO.fromDTO(dto.getChat()))
        .order(OrderDTO.fromDTO(dto.getOrder()))
        .scope(ScopeDTO.fromDTO(dto.getScope()))
        .type(TypeDTO.fromDTO(dto.getType()))
        .lastPermalink(dto.getLastPermalink())
        .build();
  }
}
