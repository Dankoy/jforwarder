package ru.dankoy.subscriptions_scheduler.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.feign.ChatFeign;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatWithSubsMapper;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

  private final ChatFeign chatFeign;
  private final ChatWithSubsMapper chatMapper;

  @Override
  public Page<Chat> findAll(boolean withSubs, Pageable pageable) {

    var dtosPage = chatFeign.getAllChats(withSubs, pageable);

    return dtosPage.map(chatMapper::fromDto);
  }
}
