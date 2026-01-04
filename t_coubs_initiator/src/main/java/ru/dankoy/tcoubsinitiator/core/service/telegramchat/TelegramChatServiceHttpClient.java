package ru.dankoy.tcoubsinitiator.core.service.telegramchat;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;
import ru.dankoy.tcoubsinitiator.core.httpservice.telegramchat.TelegramChatHttpService;

@Primary
@Service
@RequiredArgsConstructor
public class TelegramChatServiceHttpClient implements TelegramChatService {

  private final TelegramChatHttpService telegramChatHttpService;

  @Override
  public Page<Chat> getAllChats(Pageable pageable, TelegramChatFilter filter) {

    return telegramChatHttpService.getAllTelegramChats(pageable, filter);
  }
}
