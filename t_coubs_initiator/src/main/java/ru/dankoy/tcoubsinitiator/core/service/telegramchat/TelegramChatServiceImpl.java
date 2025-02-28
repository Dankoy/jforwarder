package ru.dankoy.tcoubsinitiator.core.service.telegramchat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.feign.telegramchat.TelegramChatServiceFeign;

@Service
@RequiredArgsConstructor
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatServiceFeign telegramChatServiceFeign;

  @Override
  public Page<Chat> getAllChats(Pageable pageable, String search) {

    return telegramChatServiceFeign.getAllTelegramChats(pageable, search);
  }
}
