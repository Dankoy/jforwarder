package ru.dankoy.tcoubsinitiator.core.service.telegramchat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;
import ru.dankoy.tcoubsinitiator.core.feign.telegramchat.TelegramChatServiceFeign;

@Service
@RequiredArgsConstructor
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatServiceFeign telegramChatServiceFeign;

  @Override
  public Page<Chat> getAllChats(Pageable pageable, TelegramChatFilter filter) {

    return telegramChatServiceFeign.getAllTelegramChats(pageable, filter);
  }
}
