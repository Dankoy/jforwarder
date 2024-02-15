package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.communitysub;

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
public class ChatCreateDTO {

  private long chatId;

  private String type;

  private String title;

  private String firstName;

  private String lastName;

  private String username;

  private boolean active;

  public static ChatCreateDTO toDTO(Chat chat) {

    return builder()
        .chatId(chat.getChatId())
        .type(chat.getType())
        .title(chat.getTitle())
        .firstName(chat.getFirstName())
        .lastName(chat.getLastName())
        .username(chat.getUsername())
        .active(chat.isActive())
        .build();
  }

  public static Chat fromDTO(ChatCreateDTO dto) {

    return new Chat(
        0, dto.chatId, dto.type, dto.title, dto.firstName, dto.lastName, dto.username, dto.active);
  }
}
