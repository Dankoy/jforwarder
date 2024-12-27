package ru.dankoy.subscriptions_scheduler.core.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.feign.ChatFeign;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatMapper;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

  private final ChatFeign chatFeign;
  private final ChatMapper chatMapper;

  @Override
  public Page<Chat> findAll(Pageable pageable) {

    var dtosPage = chatFeign.getAllChats(pageable);

    return dtosPage.map(chatMapper::fromDto);
  }
}
