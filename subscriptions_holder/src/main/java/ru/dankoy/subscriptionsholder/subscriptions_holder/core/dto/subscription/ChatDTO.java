package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto.subscription;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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

  @Min(1)
  private long id;

  @NotEmpty
  private long chatId;

  @NotEmpty
  private String username;

  public static ChatDTO toDTO(Chat chat) {

    return builder()
        .id(chat.getId())
        .chatId(chat.getChatId())
        .username(chat.getUserName())
        .build();

  }

  public static Chat fromDTO(ChatDTO dto) {

    return new Chat(
        dto.id,
        dto.chatId,
        dto.username
    );

  }

}
