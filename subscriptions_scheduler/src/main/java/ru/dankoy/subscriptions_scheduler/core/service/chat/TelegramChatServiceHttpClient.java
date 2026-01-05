package ru.dankoy.subscriptions_scheduler.core.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.telegramchatservice.ChatWithUUID;
import ru.dankoy.subscriptions_scheduler.core.httpservice.TelegramChatHttpService;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatWithSubsMapper;

@Slf4j
@Service("telegramChatServiceHttpClient")
@RequiredArgsConstructor
public class TelegramChatServiceHttpClient implements TelegramChatService {

  private final TelegramChatHttpService chatService;
  private final ChatWithSubsMapper chatMapper;
  private static final String SEARCH = "active:true";

  @Override
  public Page<ChatWithUUID> findAll(Pageable pageable) {

    var dtosPage = chatService.getAllChats(SEARCH, pageable);

    return dtosPage.map(chatMapper::fromDto);
  }

  @Override
  public void update(ChatWithUUID chat) {
    var withoutSubs = chatMapper.toDto(chat);
    chatService.updateChat(chat.getId(), withoutSubs);
  }
}
