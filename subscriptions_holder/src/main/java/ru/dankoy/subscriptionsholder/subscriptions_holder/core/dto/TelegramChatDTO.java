package ru.dankoy.subscriptionsholder.subscriptions_holder.core.dto;

import jakarta.validation.constraints.Min;
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

  @Min(1)
  private long id;

  @NotEmpty
  private long chatId;

  @NotEmpty
  private String username;

  public static TelegramChatDTO toDTO(TelegramChat telegramChat) {

    return builder()
        .id(telegramChat.getId())
        .chatId(telegramChat.getChatId())
        .username(telegramChat.getUserName())
        .build();

  }

  public static TelegramChat fromDTO(TelegramChatDTO dto) {

    return new TelegramChat(
        dto.id,
        dto.chatId,
        dto.username
    );

  }

}
