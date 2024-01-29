package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.tag.TagSubscription;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatCreateDTO;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscriptionCreateDTO {

  @Valid
  @NotNull
  private TagCreateDTO tag;

  @Valid
  @NotNull
  private ChatCreateDTO chat;

  @Valid
  @NotNull
  private OrderCreateDTO order;

  @Valid
  @NotNull
  private ScopeCreateDTO scope;

  @Valid
  @NotNull
  private TypeCreateDTO type;

  private String lastPermalink;


  public static TagSubscriptionCreateDTO toDTO(TagSubscription tagSubscription) {

    return new TagSubscriptionCreateDTO(
        TagCreateDTO.toDTO(tagSubscription.getTag()),
        ChatCreateDTO.toDTO(tagSubscription.getChat()),
        OrderCreateDTO.toDTO(tagSubscription.getOrder()),
        ScopeCreateDTO.toDTO(tagSubscription.getScope()),
        TypeCreateDTO.toDTO(tagSubscription.getType()),
        tagSubscription.getLastPermalink()
    );

  }

  public static TagSubscription fromDTO(TagSubscriptionCreateDTO dto) {
    return new TagSubscription(
        0,
        TagCreateDTO.fromDTO(dto.getTag()),
        ChatCreateDTO.fromDTO(dto.getChat()),
        OrderCreateDTO.fromDTO(dto.getOrder()),
        ScopeCreateDTO.fromDTO(dto.getScope()),
        TypeCreateDTO.fromDTO(dto.getType()),
        dto.getLastPermalink()
    );
  }


}
