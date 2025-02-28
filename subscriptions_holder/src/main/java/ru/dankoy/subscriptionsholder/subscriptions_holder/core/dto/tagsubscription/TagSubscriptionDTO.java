package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.tagsubscription;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
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

  @Valid @NotNull private UUID chatUuid;

  private OrderDTO order;

  private ScopeDTO scope;

  private TypeDTO type;

  private String lastPermalink;

  private LocalDateTime createdAt;

  private LocalDateTime modifiedAt;

  public static TagSubscriptionDTO toDTO(TagSub tagSubscription) {

    return new TagSubscriptionDTO(
        tagSubscription.getId(),
        TagDTO.toDTO(tagSubscription.getTag()),
        ChatDTO.toDTO(tagSubscription.getChat()),
        tagSubscription.getChatUuid(),
        OrderDTO.toDTO(tagSubscription.getOrder()),
        ScopeDTO.toDTO(tagSubscription.getScope()),
        TypeDTO.toDTO(tagSubscription.getType()),
        tagSubscription.getLastPermalink(),
        tagSubscription.getCreatedAt(),
        tagSubscription.getModifiedAt());
  }

  public static TagSub fromDTO(TagSubscriptionDTO dto) {

    return TagSub.builder()
        .id(0)
        .tag(TagDTO.fromDTO(dto.getTag()))
        .chat(ChatDTO.fromDTO(dto.getChat()))
        .chatUuid(dto.getChatUuid())
        .order(OrderDTO.fromDTO(dto.getOrder()))
        .scope(ScopeDTO.fromDTO(dto.getScope()))
        .type(TypeDTO.fromDTO(dto.getType()))
        .lastPermalink(dto.getLastPermalink())
        .createdAt(dto.getCreatedAt())
        .modifiedAt(dto.getModifiedAt())
        .build();
  }
}
