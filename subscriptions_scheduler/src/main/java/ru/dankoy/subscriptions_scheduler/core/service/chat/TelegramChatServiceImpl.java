package ru.dankoy.subscriptions_scheduler.core.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.telegramchatservice.ChatWithUUID;
import ru.dankoy.subscriptions_scheduler.core.feign.TelegramChatServiceFeign;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatWithSubsMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramChatServiceImpl implements TelegramChatService {

  private final TelegramChatServiceFeign chatFeign;
  private final ChatWithSubsMapper chatMapper;
  private static final String SEARCH = "active:true";

  @Override
  public Page<ChatWithUUID> findAll(Pageable pageable) {

    var dtosPage = chatFeign.getAllChats(SEARCH, pageable);

    return dtosPage.map(chatMapper::fromDto);
  }

  @Override
  public void update(ChatWithUUID chat) {
    var withoutSubs = chatMapper.toDto(chat);
    chatFeign.updateChat(chat.getId(), withoutSubs);
  }
}
