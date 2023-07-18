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
public class ChatCreateDTO {

  @Min(1)
  private long chatId;


  public static ChatCreateDTO toDTO(Chat chat) {

    return builder()
        .chatId(chat.getChatId())
        .build();

  }

  public static Chat fromDTO(ChatCreateDTO dto) {

    return new Chat(
        0,
        dto.chatId,
        null
    );

  }

}
