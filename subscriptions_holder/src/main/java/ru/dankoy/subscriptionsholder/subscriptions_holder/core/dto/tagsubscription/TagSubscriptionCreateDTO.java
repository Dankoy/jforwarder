package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.subscriptions.TagSub;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub.ChatCreateDTO;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagSubscriptionCreateDTO {

  @Valid @NotNull private TagCreateDTO tag;

  @Valid @NotNull private ChatCreateDTO chat;

  @Valid @NotNull private UUID chatUuid;

  @Valid @NotNull private OrderCreateDTO order;

  @Valid @NotNull private ScopeCreateDTO scope;

  @Valid @NotNull private TypeCreateDTO type;

  private String lastPermalink;

  public static TagSubscriptionCreateDTO toDTO(TagSub tagSubscription) {

    return new TagSubscriptionCreateDTO(
        TagCreateDTO.toDTO(tagSubscription.getTag()),
        ChatCreateDTO.toDTO(tagSubscription.getChat()),
        tagSubscription.getChatUuid(),
        OrderCreateDTO.toDTO(tagSubscription.getOrder()),
        ScopeCreateDTO.toDTO(tagSubscription.getScope()),
        TypeCreateDTO.toDTO(tagSubscription.getType()),
        tagSubscription.getLastPermalink());
  }

  public static TagSub fromDTO(TagSubscriptionCreateDTO dto) {
    return TagSub.builder()
        .id(0)
        .tag(TagCreateDTO.fromDTO(dto.getTag()))
        .chat(ChatCreateDTO.fromDTO(dto.getChat()))
        .chatUuid(dto.getChatUuid())
        .order(OrderCreateDTO.fromDTO(dto.getOrder()))
        .scope(ScopeCreateDTO.fromDTO(dto.getScope()))
        .type(TypeCreateDTO.fromDTO(dto.getType()))
        .lastPermalink(dto.getLastPermalink())
        .build();
  }
}
