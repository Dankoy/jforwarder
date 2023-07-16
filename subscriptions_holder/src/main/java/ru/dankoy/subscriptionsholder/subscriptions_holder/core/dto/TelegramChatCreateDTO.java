package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.dankoy.subscriptionsholder.subscriptions_holder.core.domain.TelegramChat;


@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelegramChatCreateDTO {

  @NotEmpty
  private String chatId;

  public static TelegramChatCreateDTO toDTO(TelegramChat telegramChat) {

    return builder()
        .chatId(telegramChat.getChatId())
        .build();

  }

  public static TelegramChat fromDTO(TelegramChatCreateDTO dto) {

    return new TelegramChat(
        0,
        dto.chatId
    );

  }

}
