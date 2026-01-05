package ru.dankoy.subscriptions_scheduler.core.service.chat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.httpservice.SubscriptionsHolderHttpService;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatWithSubsMapper;

@Slf4j
@Service("chatServiceHttpClient")
@RequiredArgsConstructor
public class ChatServiceHttpClient implements ChatService {

  private final SubscriptionsHolderHttpService chatHttpService;
  private final ChatWithSubsMapper chatMapper;
  private static final String SEARCH = "active:true";

  @Override
  public Page<Chat> findAll(boolean withSubs, Pageable pageable) {

    var dtosPage = chatHttpService.getAllChats(withSubs, SEARCH, pageable);

    return dtosPage.map(chatMapper::fromDto);
  }

  @Override
  public void update(Chat chat) {
    var withoutSubs = chatMapper.toDtoWithoutSubs(chat);
    chatHttpService.updateChat(chat.getId(), withoutSubs);
  }
}
