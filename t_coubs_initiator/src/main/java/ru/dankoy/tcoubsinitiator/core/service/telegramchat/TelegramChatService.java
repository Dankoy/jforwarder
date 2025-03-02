package ru.dankoy.tcoubsinitiator.core.service.telegramchat;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.Chat;
import ru.dankoy.tcoubsinitiator.core.domain.telegramchatservice.filter.TelegramChatFilter;

public interface TelegramChatService {

  public Page<Chat> getAllChats(Pageable pageable, TelegramChatFilter filter);
}
