package ru.dankoy.subscriptions_scheduler.core.service.scheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.dankoy.subscriptions_scheduler.config.properties.SchedulerProperties;
import ru.dankoy.subscriptions_scheduler.core.domain.telegramchatservice.ChatWithUUID;
import ru.dankoy.subscriptions_scheduler.core.dto.subscriptions.SubscriptionDTO;
import ru.dankoy.subscriptions_scheduler.core.service.chat.TelegramChatService;
import ru.dankoy.subscriptions_scheduler.core.service.subscriptions.SubscriptionsHolderService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramChatServiceCheckerImpl implements TelegramChatServiceChecker {

  private final TelegramChatService telegramChatService;
  private final SubscriptionsHolderService subscriptionsHolderService;
  private final SchedulerProperties schedulerProperties;

  private static final int INITIAL_PAGE_SIZE = 10;
  private static final int INITIAL_PAGE = 0;

  @Scheduled(cron = "${application.scheduler.cron:-}")
  @Override
  public void checkChats() {

    var currentPage = INITIAL_PAGE;
    var size = INITIAL_PAGE_SIZE;
    var total = Integer.MAX_VALUE;
    var withSubs = true;

    var sort = Sort.by("id").ascending();

    do {

      var pageable = PageRequest.of(currentPage, size, sort);

      var chatsPage = telegramChatService.findAll(pageable);

      List<UUID> chatUuids =
          chatsPage.getContent().stream().map(ChatWithUUID::getId).collect(Collectors.toList());

      var subscriptions =
          subscriptionsHolderService.getAllSubscriptionsByChatUuids(chatUuids, pageable);

      var chats = chatsPage.getContent();

      var map = subscriptions.stream().collect(Collectors.groupingBy(SubscriptionDTO::getChatUuid));

      log.info("Current page: {}, total: {}", currentPage + 1, chatsPage.getTotalPages());

      // if chat doesn't have subs and is older than retention days then disable it
      var now = LocalDateTime.now();
      var thresholdDate = now.minusDays(schedulerProperties.getRetention());

      var chatsToDisable =
          chats.stream()
              .filter(ChatWithUUID::isActive)
              .filter(
                  chat -> {
                    var subs = map.get(chat);
                    if (subs == null || subs.isEmpty()) {
                      return true;
                    }
                    return false;
                  })
              .filter(c -> c.getDateCreated() != null && c.getDateCreated().isBefore(thresholdDate))
              .map(
                  c ->
                      new ChatWithUUID(
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
                          c.getDateModified()))
              .collect(Collectors.toList());

      log.info("Found {} chats to disable", chatsToDisable.size());

      for (ChatWithUUID chat : chatsToDisable) {
        log.info("Disabling chat: {}", chat);
        telegramChatService.update(chat);
      }

      total = chatsPage.getTotalPages();
      currentPage++;

    } while (currentPage < total);
  }
}
