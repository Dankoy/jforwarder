package ru.dankoy.subscriptions_scheduler.core.service.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.core.domain.subscribtionsholder.Chat;
import ru.dankoy.subscriptions_scheduler.core.mapper.ChatWithSubsMapper;
import ru.dankoy.subscriptions_scheduler.core.service.chat.ChatService;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatCheckerServiceImpl implements ChatCheckerService {

  private final ChatService chatService;
  private final ChatWithSubsMapper chatWithSubsMapper;

  private static final int INITIAL_PAGE_SIZE = 10;
  private static final int INITIAL_PAGE = 0;

  @Override
  @Scheduled(initialDelay = 10000, fixedRate = 300_000) // 5 mins
  public void checkChats() {

    var currentPage = INITIAL_PAGE;
    var size = INITIAL_PAGE_SIZE;
    var total = Integer.MAX_VALUE;
    var withSubs = true;

    var pageable = PageRequest.of(currentPage, size);

    do {

      var page = chatService.findAll(withSubs, pageable);

      // if chat doesn't have subs and is older than 14 days then disable it
      var now = LocalDateTime.now();
      var thresholdDate = now.minusDays(14);
      List<Chat> chatsToDisable =
          page.getContent().stream()
              .filter(c -> c.getSubscriptions().isEmpty())
              .filter(c -> c.getDateCreated() != null && c.getDateCreated().isBefore(thresholdDate))
              .map(
                  c ->
                      new Chat(
                          c.getId(),
                          c.getChatId(),
                          c.getType(),
                          c.getTitle(),
                          c.getFirstName(),
                          c.getLastName(),
                          c.getUsername(),
                          false,
                          c.getMessageThreadId(),
                          c.getDateCreated(),
                          c.getDateModified(),
                          c.getSubscriptions()))
              .toList();

      log.info("Found {} chats to disable", chatsToDisable.size());

      for (Chat chat : chatsToDisable) {
        log.info("Disabling chat: {}", chat);
        chatService.update(chat);
      }

      total = page.getTotalPages();
      currentPage++;

    } while (currentPage <= total);
  }
}
