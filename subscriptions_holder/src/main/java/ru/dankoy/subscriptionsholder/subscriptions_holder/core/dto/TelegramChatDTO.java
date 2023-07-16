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
public class TelegramChatDTO {

  private long id;

  @NotEmpty
  private String chatId;

  public static TelegramChatDTO toDTO(TelegramChat telegramChat) {

    return builder()
        .id(telegramChat.getId())
        .chatId(telegramChat.getChatId())
        .build();

  }

  public static TelegramChat fromDTO(TelegramChatDTO dto) {

    return new TelegramChat(
        dto.id,
        dto.chatId
    );

  }

}
