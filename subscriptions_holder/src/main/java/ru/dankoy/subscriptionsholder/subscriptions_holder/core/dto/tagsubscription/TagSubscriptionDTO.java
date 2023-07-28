package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription.ChatDTO;

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


  public static TagSubscriptionDTO toDTO(TagSubscription tagSubscription) {

    return new TagSubscriptionDTO(
        tagSubscription.getId(),
        TagDTO.toDTO(tagSubscription.getTag()),
        ChatDTO.toDTO(tagSubscription.getChat()),
        OrderDTO.toDTO(tagSubscription.getOrder()),
        ScopeDTO.toDTO(tagSubscription.getScope()),
        TypeDTO.toDTO(tagSubscription.getType()),
        tagSubscription.getLastPermalink()
    );

  }

  public static TagSubscription fromDTO(TagSubscriptionDTO dto) {
    return new TagSubscription(
        dto.getId(),
        TagDTO.fromDTO(dto.getTag()),
        ChatDTO.fromDTO(dto.getChat()),
        OrderDTO.fromDTO(dto.getOrder()),
        ScopeDTO.fromDTO(dto.getScope()),
        TypeDTO.fromDTO(dto.getType()),
        dto.getLastPermalink()
    );
  }


}
