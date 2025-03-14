package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.Chat;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {

  private long id;

  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private boolean active;

  private Integer messageThreadId;

  private LocalDateTime dateCreated;

  private LocalDateTime dateModified;

  public static ChatDTO toDTO(Chat chat) {

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
        .dateCreated(chat.getDateCreated())
        .dateModified(chat.getDateModified())
        .build();
  }

  public static Chat fromDTO(ChatDTO dto) {

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
        dto.dateCreated,
        dto.dateModified);
  }
}
