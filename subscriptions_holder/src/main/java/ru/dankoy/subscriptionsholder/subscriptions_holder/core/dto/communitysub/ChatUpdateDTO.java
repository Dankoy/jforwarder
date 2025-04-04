package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatUpdateDTO {

  @Setter private long id;

  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private boolean active;

  private Integer messageThreadId;

  public static ChatUpdateDTO toDTO(Chat chat) {

    return builder()
        .id(chat.getId())
        .chatId(chat.getChatId())
        .type(chat.getType())
        .title(chat.getTitle())
        .firstName(chat.getFirstName())
        .lastName(chat.getLastName())
        .username(chat.getUsername())
        .active(chat.isActive())
        .messageThreadId(chat.getMessageThreadId())
        .build();
  }

  public static Chat fromDTO(ChatUpdateDTO dto) {

    return new Chat(
        dto.id,
        dto.chatId,
        dto.type,
        dto.title,
        dto.firstName,
        dto.lastName,
        dto.username,
        dto.active,
        dto.messageThreadId,
        null,
        null);
  }
}
