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
public class TelegramChatCreateDTO {

  @Min(1)
  private long chatId;

  @NotEmpty
  private String userName;


  public static TelegramChatCreateDTO toDTO(TelegramChat telegramChat) {

    return builder()
        .chatId(telegramChat.getChatId())
        .userName(telegramChat.getUserName())
        .build();

  }

  public static TelegramChat fromDTO(TelegramChatCreateDTO dto) {

    return new TelegramChat(
        0,
        dto.chatId,
        dto.userName
    );

  }

}
