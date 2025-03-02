package ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TelegramChatFilter {

  private UUID id;
  private Long chatId;
  private String type;
  private String title;
  private String firstName;
  private String lastName;
  private String username;
  private Boolean active;
  private Integer messageThreadId;
}
